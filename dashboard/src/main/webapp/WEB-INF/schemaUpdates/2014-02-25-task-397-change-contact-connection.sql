DROP PROCEDURE IF EXISTS removeZombieCollections;
DROP PROCEDURE IF EXISTS repairStoryPermissions;
DROP PROCEDURE IF EXISTS finalDocUpdates;
DROP PROCEDURE IF EXISTS updateStoriesOwner;
DROP PROCEDURE IF EXISTS updateDocumentContribs;
DROP PROCEDURE IF EXISTS copyNotesBy;
DROP PROCEDURE IF EXISTS copyNotesAbout;
DROP PROCEDURE IF EXISTS generateUniquePersons;
DROP PROCEDURE IF EXISTS generateUniquePersonsForUsers;
DROP PROCEDURE IF EXISTS copyAddressLinks;
DROP PROCEDURE IF EXISTS copyContacts;
DROP PROCEDURE IF EXISTS grantsOnNewPersons;
DROP PROCEDURE IF EXISTS grantsToNewPersons;
DROP PROCEDURE IF EXISTS clearMultiPersons;
DROP PROCEDURE IF EXISTS shiftSinglePersons;
DROP PROCEDURE IF EXISTS deleteZombiePeople;
DROP PROCEDURE IF EXISTS setupRootProfiles;

-- 1) The direct grants on root predate the current super user auth
-- check, and are unnecassary.
DELETE FROM acl_entry
WHERE sid = 0;

-- 1) Many Entities are the subject of multilpe, redundant
-- grants. It's not generally a problem, but in order to separate out
-- a single Person into 1-Person-per-Org, we want to ask whihc Persons
-- have multiple grants tying them to multiple orgs, so we simplify by
-- deleting the extraneous grants.
DELETE acl1.*
FROM acl_entry acl1
  JOIN acl_entry acl2 ON acl1.acl_object_identity = acl2.acl_object_identity
                         AND acl1.sid = acl2.sid
                         AND acl1.mask = acl2.mask
WHERE acl1.id > acl2.id;

-- 2) Clear out single ACL entry granted to a non-user person.
DELETE p.*
FROM person p
  JOIN acl_entry a ON p.id = a.sid
  LEFT OUTER JOIN user u ON p.id = u.id
WHERE u.id IS NULL;

-- 3) First set of alterations to the new model. We also add a
-- temporary 'sourcePerson' field to track where a new per-org-Person
-- was copied from. Note, some constraints will be added after the
-- data is updated.
ALTER TABLE user DROP FOREIGN KEY fk_user_person;
ALTER TABLE user ADD CONSTRAINT fk_user_entity FOREIGN KEY (id) REFERENCES entity (id);

ALTER TABLE person ADD COLUMN user INT(11);
ALTER TABLE person ADD CONSTRAINT fk_person_user FOREIGN KEY (user) REFERENCES user (id);
ALTER TABLE person ADD COLUMN organization INT(11);
-- Temporary tracking table so we know when we've generated the new
-- Person for a given Organization.
ALTER TABLE person ADD COLUMN sourcePerson INT;
ALTER TABLE person ADD CONSTRAINT uniq_profile_user_org UNIQUE (user, organization);
ALTER TABLE documentContributor DROP FOREIGN KEY fk_documentContributor_user1;
-- A temporary tracking field to make sure we preserve all notes.
ALTER TABLE document ADD COLUMN copied INT DEFAULT NULL;
DROP TRIGGER IF EXISTS create_acl_sid_user;
DROP TRIGGER IF EXISTS create_acl_sid_organization;
-- New triggers created after all this runs; but we drop them here to
-- support testing wherein they may have already been created as part
-- of the reset_and_load_database.sh script.
DROP TRIGGER IF EXISTS acl_sid_person_user_create;
DROP TRIGGER IF EXISTS acl_sid_person_user_udpate;
DROP TRIGGER IF EXISTS acl_sid_person_user_delete;

-- When we copy notes, the single original may result in multiplle
-- copies. To retain the chain back to the import records, we have to
-- change the constraint.
ALTER TABLE importRecord DROP INDEX importRecord_unique;
ALTER TABLE importRecord ADD COLUMN toDelete TINYINT(1);

-- X) The import set the owner on Stories correctly, but not the associated 'BODY' Documents.
UPDATE story s
  JOIN document d ON s.id = d.systemEntity
  JOIN importRecord ir ON s.id = ir.targetId
  JOIN systemEntity de ON de.id = d.id
  JOIN systemEntity se ON se.id = s.id
SET de.owner = se.owner, d.primaryAuthor = se.owner, s.owner = se.owner
WHERE de.owner = 0 AND d.systemEntityRelation IN ('BODY', 'ANSWER_SET');

DELIMITER //

-- There are 7 zombie collections, owned by Users (should be
-- Organizations) or with NULL owners. None recent and so I suspect
-- this was a bug. None have any stories associated, though there are
-- questionnaires.
CREATE PROCEDURE removeZombieCollections()
  BEGIN
    DECLARE collectionIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT c.id
      FROM collection c
        JOIN systemEntity ce ON c.id = ce.id
        JOIN entity e ON ce.id = e.id
        LEFT JOIN organization o ON o.id = ce.owner
      WHERE o.id IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO collectionIdX;
      IF NOT done
      THEN
        DELETE FROM tag
        WHERE systemEntity = collectionIdX;
        DELETE FROM contentL10n
        WHERE questionnaire = collectionIdX;
        DELETE FROM formElement
        WHERE questionnaire = collectionIdX;
        DELETE c.*, e.*, se.*
        FROM collection c
          JOIN entity e ON e.id = c.id
          JOIN systemEntity se ON c.id = se.id
          LEFT JOIN organization o ON o.id = se.owner
        WHERE o.id IS NULL AND c.id = collectionIdX;
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

-- We actually have to update primaryAuthor and owner seperately. Note
-- that we verified that the 'story.owner' and 'systemEntity.owner'
-- fields are in synchronization and that is not a concern.
CREATE PROCEDURE updateStoriesOwner(IN newProfileIdX INT, IN userIdX INT, IN organizationIdX INT)
  BEGIN
    DECLARE storyIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT DISTINCT (s.id)
      FROM story s
        JOIN collection_story cs ON cs.story = s.id
        JOIN collection c ON cs.collection = c.id
        JOIN systemEntity ce ON c.id = ce.id
      WHERE s.owner = userIdX AND ce.owner = organizationIdX;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO storyIdX;
      IF NOT done
      THEN
        UPDATE story
        SET owner = newProfileIdX
        WHERE id = storyIdX;
        UPDATE systemEntity
        SET owner = newProfileIdX
        WHERE id = storyIdX;
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

-- Why not just update all the documents? Because we want to make sure
-- we're matching the new Profile with the Organization.
CREATE PROCEDURE updateDocumentContribs(IN newProfileIdX INT, IN userIdX INT, IN organizationIdX INT)
  BEGIN
    -- Documents attached to Stories.
    UPDATE document d
      JOIN story s ON s.id = d.systemEntity
      JOIN collection_story cs ON cs.story = s.id
      JOIN systemEntity ce ON cs.collection = ce.id
    SET d.primaryAuthor = newProfileIdX
    WHERE d.primaryAuthor = userIdX AND ce.owner = organizationIdX;
    UPDATE documentContributor dc
      JOIN document d ON dc.document = d.id
      JOIN story s ON s.id = d.systemEntity
      JOIN collection_story cs ON cs.story = s.id
      JOIN systemEntity ce ON cs.collection = ce.id
    SET dc.user = newProfileIdX
    WHERE dc.user = userIdX AND ce.owner = organizationIdX;
    -- Documents attached to Collections.
    UPDATE document d
      JOIN collection c ON c.id = d.systemEntity
      JOIN systemEntity ce ON c.id = ce.id
    SET d.primaryAuthor = newProfileIdX
    WHERE d.primaryAuthor = userIdX AND ce.owner = organizationIdX;
    UPDATE documentContributor dc
      JOIN document d ON dc.document = d.id
      JOIN collection c ON c.id = d.systemEntity
      JOIN systemEntity ce ON c.id = ce.id
    SET dc.user = newProfileIdX
    WHERE dc.user = userIdX AND ce.owner = organizationIdX;
    -- Documents attached to Organizations.
    UPDATE document d
      JOIN organization o ON o.id = d.systemEntity
    SET d.primaryAuthor = newProfileIdX
    WHERE d.primaryAuthor = userIdX AND o.id = organizationIdX;
    UPDATE documentContributor dc
      JOIN document d ON dc.document = d.id
      JOIN organization o ON o.id = d.systemEntity
    SET dc.user = newProfileIdX
    WHERE dc.user = userIdX AND o.id = organizationIdX;
    -- Documents attached to Persons.
    UPDATE document d
      JOIN person p ON p.id = d.systemEntity
    SET d.primaryAuthor = newProfileIdX
    WHERE d.primaryAuthor = userIdX AND p.organization = organizationIdX;
    UPDATE documentContributor dc
      JOIN document d ON dc.document = d.id
      JOIN person p ON p.id = d.systemEntity
    SET dc.user = newProfileIdX
    WHERE dc.user = userIdX AND p.organization = organizationIdX;
  END//

-- We copy any notes associated to the original single Person to each Profile
CREATE PROCEDURE copyNotesBy(IN newProfileIdX INT, IN origPersonIdX INT, IN organizationIdX INT)
  BEGIN
    DECLARE noteIdX, textVersionX, systemEntityX INT;
    DECLARE titleX, permalinkX VARCHAR(255);
    DECLARE publicX TINYINT(1);
    DECLARE textTypeX VARCHAR(16);
    DECLARE textX TEXT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        d.id,
        dt.version,
        d.title,
        d.permalink,
        d.public,
        d.systemEntity,
        dt.textType,
        dt.text
      FROM document d
        JOIN documentText dt ON dt.documentId = d.id
        JOIN systemEntity de ON d.id = de.id
      WHERE d.primaryAuthor = origPersonIdX AND d.systemEntityRelation = 'NOTE' AND d.copied IS NULL;
    -- If already copied means it has 'old' references; if we copy that,
    -- then we'll potentiall be replicating the old data in a non-copy
    -- which won't get cleaned up.
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO noteIdX, textVersionX, titleX, permalinkX, publicX, systemEntityX, textTypeX, textX;
      IF NOT done
      THEN
        INSERT INTO systemEntity (owner) VALUES (organizationIdX);
        INSERT INTO document (id, primaryAuthor, title, permalink, public, systemEntity, systemEntityRelation)
        VALUES (LAST_INSERT_ID(), newProfileIdX, titleX, CONCAT(permalinkX, organizationIdX), publicX, systemEntityX,
                'NOTE');
        INSERT INTO documentText (documentId, version, textType, text)
        VALUES (LAST_INSERT_ID(), textVersionX, textTypeX, textX);
        UPDATE document
        SET copied = LAST_INSERT_ID()
        WHERE id = noteIdX;
        INSERT INTO importRecord (sourceId, sourceTable, targetId, targetTable)
          SELECT
            sourceId,
            sourceTable,
            LAST_INSERT_ID(),
            'document'
          FROM importRecord
          WHERE targetId = noteIdX;
        UPDATE importRecord
        SET toDelete = 1
        WHERE targetId = noteIdX;
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE copyNotesAbout(IN newProfileIdX INT, IN origPersonIdX INT, IN organizationIdX INT)
  BEGIN
    DECLARE noteIdX, primaryAuthorX, textVersionX, systemEntityX INT;
    DECLARE titleX, permalinkX VARCHAR(255);
    DECLARE publicX TINYINT(1);
    DECLARE textTypeX VARCHAR(16);
    DECLARE textX TEXT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        d.id,
        dt.version,
        d.primaryAuthor,
        d.title,
        d.permalink,
        d.public,
        d.systemEntity,
        dt.textType,
        dt.text
      FROM document d
        JOIN documentText dt ON dt.documentId = d.id
        JOIN systemEntity de ON d.id = de.id
      WHERE d.systemEntity = origPersonIdX AND d.systemEntityRelation = 'NOTE' AND d.copied IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO noteIdX, textVersionX, primaryAuthorX, titleX, permalinkX, publicX, systemEntityX, textTypeX, textX;
      IF NOT done
      THEN
        INSERT INTO systemEntity (owner) VALUES (organizationIdX);
        INSERT INTO document (id, primaryAuthor, title, permalink, public, systemEntity, systemEntityRelation)
        VALUES (LAST_INSERT_ID(), primaryAuthorX, titleX, CONCAT(permalinkX, organizationIdX), publicX, newProfileIdX,
                'NOTE');
        INSERT INTO documentText (documentId, version, textType, text)
        VALUES (LAST_INSERT_ID(), textVersionX, textTypeX, textX);
        UPDATE document
        SET copied = LAST_INSERT_ID()
        WHERE id = noteIdX;
        INSERT INTO importRecord (sourceId, sourceTable, targetId, targetTable)
          SELECT
            sourceId,
            sourceTable,
            LAST_INSERT_ID(),
            'document'
          FROM importRecord
          WHERE targetId = noteIdX;
        UPDATE importRecord
        SET toDelete = 1
        WHERE targetId = noteIdX;
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

-- 4) We look at those Persons which have associations (thorugh the
-- 'acl_entry' table, using 'READ' as a definitive guide) with more
-- than one Organization. We originally did this by looking at all
-- persons and generating for 1+ orgs, but it took 2+ hours to run. By
-- only changing the Persons with multiple associations, we bring
-- total execution time to under 10 min (on local workstation). We
-- also handle the Contact associations here, copying them from the
-- single source and into each of the new 'per-org-Persons'.
--
-- The idea is that if a Person has only one association, then there
-- is no need to multiplex the identity, it can just stay with the
-- current Org. If there are more than one associations, we will
-- create a new identity for each and then delete the original.
CREATE PROCEDURE generateUniquePersons()
  BEGIN
    DECLARE personIdX, organizationIdX, userIdX, new_person_id INT;
    DECLARE surnameX, givenNameX VARCHAR(512);
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT DISTINCT
        (p.id),
        p.surname,
        p.givenName,
        o1.id
      FROM person p
        JOIN acl_entry acl1 ON p.id = acl1.acl_object_identity
        JOIN organization o1 ON o1.id = acl1.sid
        JOIN acl_entry acl2 ON p.id = acl2.acl_object_identity
        JOIN organization o2 ON o2.id = acl2.sid
        LEFT OUTER JOIN user u ON p.id = u.id
      WHERE o1.id != o2.id AND u.id IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO personIdX, surnameX, givenNameX, organizationIdX;
      IF NOT done
      THEN
        -- Create org specific person, as necessary.
        SET new_person_id =
        (SELECT id
         FROM person
         WHERE sourcePerson = personIdX AND organization = organizationIdX);
        --      IF new_person_id IS NULL THEN
        INSERT INTO systemEntity (version, public) VALUES (1, 0);
        SET new_person_id = (SELECT LAST_INSERT_ID()
                             FROM dual);
        INSERT INTO acl_sid VALUES (new_person_id, 1, new_person_id);
        UPDATE systemEntity
        SET owner = new_person_id
        WHERE id = new_person_id;
        INSERT INTO entity (id) VALUES (new_person_id);
        INSERT INTO person (id, surname, givenName, convioSyncStatus, user, organization, sourcePerson)
        VALUES (new_person_id, surnameX, givenNameX, NULL, NULL, organizationIdX, personIdX);
        -- Update references.
        CALL updateStoriesOwner(new_person_id, personIdX, organizationIdX);
        CALL updateDocumentContribs(new_person_id, personIdX, organizationIdX);
        CALL copyNotesBy(new_person_id, personIdX, organizationIdX);
        CALL copyNotesAbout(new_person_id, personIdX, organizationIdX);
        UPDATE importRecord
        SET targetId = new_person_id
        WHERE targetId = personIdX;
      --      END IF; -- org_person_exists = 0
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

-- 5) Next, we handle users that can operato on multiple
-- organizations. It may be possible to combine this step with the
-- previous, but this works well enough. Again, contacts are handled
-- as needed. This method is compatible with the first (may actually
-- be unecessary, but seems to work and not worth testing since this
-- is a one shot deal).
CREATE PROCEDURE generateUniquePersonsForUsers()
  BEGIN
    DECLARE userIdX, organizationIdX, new_person_id INT;
    DECLARE surnameX, givenNameX VARCHAR(512);
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        pId,
        surname,
        givenName,
        oId
      FROM
        ((SELECT
            p.id AS pId,
            p.surname,
            p.givenName,
            o.id AS oId
          FROM user u
            JOIN person p ON u.id = p.id
            JOIN acl_entry acl ON u.id = acl.sid
            JOIN organization o ON o.id = acl.acl_object_identity
          WHERE u.id != 0)
          UNION
          (SELECT p.id AS pId, p.surname, p.givenName, o.id AS oId
          FROM USER u
          JOIN person p ON u.id=p.id
          JOIN acl_entry acl ON u.id=acl.acl_object_identity
          JOIN organization o ON o.id=acl.sid
          WHERE u.id!=0)) AS tmp;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO userIdX, surnameX, givenNameX, organizationIdX;
      IF NOT done
      THEN
        -- Create org specific person, as necessary.
        SET new_person_id =
        (SELECT id
         FROM person
         WHERE sourcePerson = userIdX AND organization = organizationIdX);
        IF new_person_id IS NULL
        THEN
          INSERT INTO systemEntity (version, public) VALUES (1, 0);
          INSERT INTO acl_sid VALUES (LAST_INSERT_ID(), 1, LAST_INSERT_ID());
          UPDATE systemEntity
          SET owner = LAST_INSERT_ID()
          WHERE id = LAST_INSERT_ID();
          INSERT INTO entity (id) VALUES (LAST_INSERT_ID());
          INSERT INTO person (id, surname, givenName, convioSyncStatus, user, organization, sourcePerson)
          VALUES (LAST_INSERT_ID(), surnameX, givenNameX, NULL, userIdX, organizationIdX, userIdX);
          -- Update references.
          SET new_person_id = (SELECT LAST_INSERT_ID()
                               FROM dual);
          CALL updateStoriesOwner(new_person_id, userIdX, organizationIdX);
          CALL updateDocumentContribs(new_person_id, userIdX, organizationIdX);
          CALL copyNotesBy(new_person_id, userIdX, organizationIdX);
          CALL copyNotesAbout(new_person_id, userIdX, organizationIdX);
        END IF; -- new_person_id is NULL
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE copyContacts()
  BEGIN
    DECLARE newIdX, contactIdxX INT;
    DECLARE contactMediumX, contactTypeX VARCHAR(32);
    DECLARE surnameX, givenNameX, contactValueX VARCHAR(512);
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        p.id,
        c.medium,
        c.type,
        c.value,
        c.idx
      FROM person p
        JOIN contact c ON p.sourcePerson = c.entityId;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO newIdX, contactMediumX, contactTypeX, contactValueX, contactIdxX;
      IF NOT done
      THEN
        -- Copy contacts. We can get multiple contacts.
  --      SET contact_count=
  --        (SELECT COUNT(*) FROM contact WHERE entityId=new_person_id AND medium=contactMediumX AND type=contactTypeX AND value=contactValueX);
  --      IF contact_count = 0 THEN
        INSERT INTO contact (entityId, medium, type, value, idx)
        VALUES (newIdX, contactMediumX, contactTypeX, contactValueX, contactIdxX);
      --      END IF;
      END IF;
      -- NOT done
    UNTIL done END REPEAT;
  END//

-- 6) Now, for all new Persons, we copy the original Addresses
-- associated with the source and recreate for each of the new
-- per-org-Persons.
CREATE PROCEDURE copyAddressLinks()
  BEGIN
    DECLARE origIdX, newIdX, idxX INT;
    DECLARE relationX, postalCodeX VARCHAR(64);
    DECLARE address1X, address2X VARCHAR(255);
    DECLARE cityX VARCHAR(128);
    DECLARE stateX VARCHAR(3);
    DECLARE countryX VARCHAR(2);
    DECLARE geoCodeStatusX, geoCodeProviderX VARCHAR(16);
    DECLARE geoCodeDateX DATETIME;
    DECLARE latitudeX, longitudeX DECIMAL(10, 7);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        origP.id,
        newP.id,
        relation,
        address1,
        address2,
        city,
        state,
        country,
        postalCode,
        latitude,
        longitude,
        idx,
        geoCodeStatus,
        geoCodeProvider,
        geoCodeDate
      FROM person origP
        JOIN person newP ON newP.sourcePerson = origP.id
        JOIN address a ON origP.id = a.entity;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO origIdX, newIdX, relationX, address1X, address2X, cityX,
        stateX, countryX, postalCodeX, latitudeX, longitudeX, idxX,
        geoCodeStatusX, geoCodeProviderX, geoCodeDateX;
      IF NOT done
      THEN
        INSERT INTO address
        (entity, relation, address1, address2, city, state,
         country, postalCode, latitude, longitude, idx, geoCodeStatus,
         geoCodeProvider, geoCodeDate)
        VALUES (newIdX, relationX, address1X, address2X, cityX, stateX,
                countryX, postalCodeX, latitudeX, longitudeX, idxX, geoCodeStatusX,
                geoCodeProviderX, geoCodeDateX);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

-- 7) Same with grants. We copy the grants for each org associated to the original Person and replicate with the new per-org-Person. Note that the other methods copy each Address and Contact as 1-many. One original address gets copied for (orig) Person to (new) Person A-Org A and (new) Person B-Org B. Here, the grants are split up, so grant on Orign Person - Org A goes to new Person A, but not new Person B.
CREATE PROCEDURE grantsOnNewPersons()
  BEGIN
    DECLARE origAclIdX, newIdX, sidX, maskX INT;
    DECLARE grantingX, audit_successX, audit_failureX TINYINT(1);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        acl.id,
        p.id,
        acl.sid,
        acl.mask,
        acl.granting,
        acl.audit_success,
        acl.audit_failure
      FROM person p
        JOIN acl_entry acl ON p.sourcePerson = acl.acl_object_identity
      WHERE p.sourcePerson IS NOT NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO origAclIdX, newIdX, sidX, maskX, grantingX, audit_successX, audit_failureX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT IFNULL(MAX(ace_order), -1)
                          FROM acl_entry
                          WHERE acl_object_identity = newIdX) + 1;
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
        VALUES (newIdX, @ace_order, sidX, maskX, grantingX, audit_successX, audit_failureX);
        DELETE FROM acl_entry
        WHERE id = origAclIdX;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE grantsToNewPersons()
  BEGIN
    DECLARE origAclIdX, newIdX, objectIdX, maskX INT;
    DECLARE grantingX, audit_successX, audit_failureX TINYINT(1);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        acl.id,
        p.id,
        acl.acl_object_identity,
        acl.mask,
        acl.granting,
        acl.audit_success,
        acl.audit_failure
      FROM person p
        JOIN acl_entry acl ON p.sourcePerson = acl.sid
      WHERE p.sourcePerson IS NOT NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO origAclIdX, newIdX, objectIdX, maskX, grantingX, audit_successX, audit_failureX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT MAX(ace_order)
                          FROM acl_entry
                          WHERE acl_object_identity = objectIdX) + 1;
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
        VALUES (objectIdX, @ace_order, newIdX, maskX, grantingX, audit_successX, audit_failureX);
        DELETE FROM acl_entry
        WHERE id = origAclIdX;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

-- 8) Now we delete all the old Persons which were multiplexed.
CREATE PROCEDURE clearMultiPersons()
  BEGIN
    DECLARE personIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT p.sourcePerson
      FROM person p
      WHERE p.sourcePerson IS NOT NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO personIdX;
      IF NOT done
      THEN
        DELETE FROM contact
        WHERE entityId = personIdX;
        DELETE FROM address
        WHERE entity = personIdX;
        DELETE FROM acl_entry
        WHERE acl_object_identity = personIdX OR sid = personIdX;
        DELETE FROM acl_object_identity
        WHERE id = personIdX;
        DELETE FROM acl_sid
        WHERE sid = personIdX AND sid != 0;
        DELETE FROM person
        WHERE id = personIdX;
        -- There may be a user associated to the original person, in
        -- which case we want to preserve the entity and system entity.
        SET @user_count := (SELECT COUNT(*)
                            FROM user
                            WHERE id = personIdX);
        IF @user_count = 0
        THEN
          DELETE FROM entity
          WHERE id = personIdX;
          DELETE FROM systemEntity
          WHERE id = personIdX;
        END IF;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

-- X) Now, we shift the user ACLs associated with a single user-person
-- from the user to the person. Recall that the previous methods all
-- dealt with users requiring multiple orgs.
CREATE PROCEDURE shiftSinglePersons()
  BEGIN
    DECLARE userIdX, personIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        u.id,
        p.id
      FROM user u
        JOIN person p ON p.user = u.id
        LEFT JOIN person p2 ON p2.user = u.id
      WHERE p2.id IS NULL AND u.id != 0;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO userIdX, personIdX;
      IF NOT done
      THEN
        UPDATE acl_entry
        SET sid = personIdX
        WHERE sid = userIdX;
        UPDATE document
        SET primaryAuthor = personIdX
        WHERE primaryAuthor = userIdX;
        UPDATE documentContributor
        SET user = personIdX
        WHERE user = userIdX;
        UPDATE address
        SET entity = personIdX
        WHERE entity = userIdX;
        UPDATE contact
        SET entityId = personIdX
        WHERE entityId = userIdX;
        UPDATE systemEntity
        SET owner = personIdX
        WHERE owner = userIdX;
        UPDATE story
        SET owenr = personIdX
        WHERE owner = userIdX;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE setupRootProfiles()
  BEGIN
    DECLARE orgIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT id
      FROM organization;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO orgIdX;
      IF NOT done
      THEN
        -- Create root profiles.
        INSERT INTO systemEntity (version, public) VALUES (1, 0);
        INSERT INTO acl_sid VALUES (LAST_INSERT_ID(), 1, LAST_INSERT_ID());
        UPDATE systemEntity
        SET owner = LAST_INSERT_ID()
        WHERE id = LAST_INSERT_ID();
        INSERT INTO entity (id) VALUES (LAST_INSERT_ID());
        INSERT INTO person (id, surname, givenName, convioSyncStatus, user, organization)
        VALUES (LAST_INSERT_ID(), 'root', 'root', NULL, 0, orgIdX);
        -- documents attributed to root
        UPDATE document d
          JOIN systemEntity de ON d.id = de.id
          JOIN story s ON d.systemEntity = s.id
          JOIN collection_story cs ON s.id = cs.story
          JOIN collection c ON c.id = cs.collection
          JOIN systemEntity ce ON ce.id = c.id
          JOIN documentContributor dc ON dc.document = d.id
        SET de.owner = LAST_INSERT_ID(), d.primaryAuthor = LAST_INSERT_ID(), s.owner = LAST_INSERT_ID(),
          dc.user    = LAST_INSERT_ID()
        WHERE de.owner = 0;
        UPDATE document d
          JOIN systemEntity de ON d.id = de.id
          JOIN story s ON d.systemEntity = s.id
          JOIN collection_story cs ON s.id = cs.story
          JOIN collection c ON c.id = cs.collection
          JOIN systemEntity ce ON ce.id = c.id
        SET de.owner = LAST_INSERT_ID(), d.primaryAuthor = LAST_INSERT_ID(), s.owner = LAST_INSERT_ID()
        WHERE de.owner = 0;
        -- notes regarding people
        UPDATE document d
          JOIN documentContributor dc ON d.id = dc.document
          JOIN person p ON d.systemEntity = p.id
        SET d.primaryAuthor = LAST_INSERT_ID(), dc.user = LAST_INSERT_ID()
        WHERE d.primaryAuthor = 0 AND p.organization = orgIdX
              AND d.systemEntityRelation IN ('COMMUNICATION', 'NOTE', 'ATTACHMENT');
        UPDATE document d
          JOIN person p ON d.systemEntity = p.id
        SET d.primaryAuthor = LAST_INSERT_ID()
        WHERE d.primaryAuthor = 0 AND p.organization = orgIdX
              AND d.systemEntityRelation IN ('COMMUNICATION', 'NOTE', 'ATTACHMENT');
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE deleteZombiePeople()
  BEGIN
    DECLARE personIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT p.id
      FROM person p
        LEFT JOIN acl_entry acl ON acl.acl_object_identity = p.id
      WHERE acl.id IS NULL AND p.organization IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO personIdX;
      IF NOT done
      THEN
        DELETE FROM contact
        WHERE entityId = personIdX;
        DELETE FROM address
        WHERE entity = personIdX;
        DELETE FROM person
        WHERE id = personIdX;
        DELETE FROM entity
        WHERE id = personIdX;
        DELETE FROM systemEntity
        WHERE id = personIdX;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE repairStoryPermissions()
  BEGIN
    DECLARE personIdX, organizationIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        p.id,
        o.id
      FROM person p
        JOIN document d ON d.primaryAuthor = p.id
        JOIN story s ON d.systemEntity = s.id
        JOIN collection_story cs ON s.id = cs.story
        JOIN systemEntity ce ON ce.id = cs.collection
        JOIN organization o ON ce.owner = o.id
        LEFT JOIN acl_entry acl ON acl.acl_object_identity = p.id AND o.id = acl.sid
      WHERE d.systemEntityRelation = 'BODY' AND acl.id IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO personIdX, organizationIdX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT IFNULL(MAX(ace_order), -1)
                          FROM acl_entry
                          WHERE acl_object_identity = personIdX) + 1;
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
        VALUES (personIdX, @ace_order, organizationIdX, 1, 1, 0, 0),
          (personIdX, @ace_order + 1, organizationIdX, 2, 1, 0, 0),
          (personIdX, @ace_order + 2, organizationIdX, 16, 1, 0, 0);
      END IF;
    UNTIL done END REPEAT;
  END//

-- There is one update of new Profile authors that doesn't get
-- made. An attachment to a single-non-user-person. Not sure why, but
-- easier to just fix. I go ahead and make a general purpose fix just
-- in case it's an issue with the updated data.
CREATE PROCEDURE finalDocUpdates()
  BEGIN
    DECLARE newIdX, userIdX, organizationIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        newP.id,
        u.id,
        targetP.organization
      FROM user u
        JOIN person newP ON newP.user = u.id
        JOIN document d ON d.primaryAuthor = u.id
        JOIN person targetP ON targetP.id = d.systemEntity AND targetP.organization = newP.organization;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO newIdX, userIdX, organizationIdX;
      IF NOT done
      THEN
        CALL updateDocumentContribs(newIdX, userIdX, organizationIdX);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL removeZombieCollections();
DROP PROCEDURE removeZombieCollections;
CALL repairStoryPermissions();
DROP PROCEDURE repairStoryPermissions;
CALL generateUniquePersonsForUsers();
DROP PROCEDURE generateUniquePersonsForUsers;
CALL generateUniquePersons();
DROP PROCEDURE generateUniquePersons;
DELETE FROM importRecord
WHERE toDelete = 1;
DELETE dt.* FROM documentText dt JOIN document d ON d.id = dt.documentID
WHERE d.copied IS NOT NULL;
DELETE dc.* FROM documentContributor dc JOIN document d ON dc.document = d.id
WHERE d.copied IS NOT NULL;
DELETE d.*, e.* FROM document d JOIN systemEntity e ON e.id = d.id
WHERE d.copied IS NOT NULL;
CALL copyAddressLinks();
DROP PROCEDURE copyAddressLinks;
CALL copyContacts();
DROP PROCEDURE copyContacts;
CALL grantsOnNewPersons();
DROP PROCEDURE grantsOnNewPersons;
CALL grantsToNewPersons();
DROP PROCEDURE grantsToNewPersons;
-- There is one story that's not in any collection and has no documents so it's still connected to the owner. We clear that one out
DELETE s.*
FROM story s
  JOIN person p ON p.sourcePerson = s.owner
  LEFT JOIN document d ON d.systemEntity = s.id
  LEFT JOIN collection_story cs ON s.id = cs.story
WHERE d.id IS NULL AND cs.collection IS NULL;
CALL shiftSinglePersons();
DROP PROCEDURE shiftSinglePersons;
CALL clearMultiPersons();
DROP PROCEDURE clearMultiPersons;

-- Delete all Stories that have no Documents
DELETE cs.* FROM collection_story cs JOIN story s ON cs.story = s.id
  LEFT JOIN document d ON d.systemEntity = s.id
WHERE d.id IS NULL;
DELETE s.* FROM story s LEFT JOIN document d ON d.systemEntity = s.id
WHERE d.id IS NULL;

-- CALL deleteZombiePeople();
DROP PROCEDURE deleteZombiePeople;

-- X) Update the 'organization' for persons related to single organization.
-- UPDATE person p
--     JOIN document d ON d.primaryAuthor=p.id
--    JOIN story s ON s.id=d.systemEntity
--    JOIN collection_story cs ON s.id=cs.story
--    JOIN systemEntity ce ON ce.id=cs.collection
--  SET p.organization=ce.owner
--  WHERE p.organization is null;
UPDATE person p
  JOIN acl_entry acl ON acl.acl_object_identity = p.id
  JOIN organization o ON acl.sid = o.id
  LEFT JOIN acl_entry acl2
    ON acl.acl_object_identity = acl2.acl_object_identity AND acl.sid = acl2.sid AND acl.mask > acl2.mask
SET p.organization = acl.sid
WHERE p.organization IS NULL;

CALL setupRootProfiles();
DROP PROCEDURE setupRootProfiles;

-- update the default waiver entries
UPDATE document d JOIN person p ON p.organization = d.systemEntity
  JOIN user u ON p.user = u.id
SET d.primaryAuthor = p.id
WHERE u.id = 0 AND d.systemEntityRelation = 'DEFAULT_PERMISSIONS';
-- Custom data fixes / massages (3 records on z)
UPDATE document d JOIN story s ON d.systemEntity = s.id
  JOIN systemEntity se ON s.id = se.id
SET d.primaryAuthor = se.owner
WHERE d.primaryAuthor = 0;

DELETE FROM person
WHERE organization IS NULL;

-- Doc ref'ing Person doesn't get update; not sure why, but this fixes it.
CALL finalDocUpdates();
DROP PROCEDURE finalDocUpdates;

-- Drop temporary columns.
ALTER TABLE person DROP COLUMN sourcePerson;
ALTER TABLE document DROP COLUMN copied;
-- Setup data constraints.
ALTER TABLE person MODIFY COLUMN organization INT(11) NOT NULL;
ALTER TABLE person ADD CONSTRAINT fk_person_org FOREIGN KEY (organization) REFERENCES organization (id);
ALTER TABLE documentContributor ADD CONSTRAINT fk_documentContributer_profile FOREIGN KEY (user) REFERENCES person (id);

DELETE u.* FROM user u JOIN person p ON p.id = u.id
  JOIN document d ON d.primaryAuthor = p.id;
-- Delete original People tied directly to User.
DELETE FROM person
WHERE id IN (SELECT id
             FROM user);

DELIMITER //
CREATE TRIGGER acl_sid_person_user_create AFTER INSERT ON person
FOR EACH ROW
  BEGIN
    IF NEW.user IS NOT NULL
    THEN
      BEGIN
        SET @existing = (SELECT COUNT(*)
                         FROM acl_sid
                         WHERE id = NEW.user);
        IF @existing = 0
        THEN
          INSERT INTO acl_sid (id, principal, sid) VALUES (new.id, 1, new.id);
        END IF;
      END;
    END IF;
  END;
//

CREATE TRIGGER acl_sid_person_user_udpate AFTER UPDATE ON person
FOR EACH ROW
  BEGIN
    IF NEW.user IS NOT NULL
    THEN
      BEGIN
        SET @existing = (SELECT COUNT(*)
                         FROM acl_sid
                         WHERE id = NEW.user);
        IF @existing = 0
        THEN
          INSERT INTO acl_sid (id, principal, sid) VALUES (new.id, 1, new.id);
        END IF;
      END;
    END IF;
  END;
//

CREATE TRIGGER acl_sid_person_user_delete AFTER DELETE ON person
FOR EACH ROW
  BEGIN
    IF OLD.user IS NOT NULL
    THEN
      BEGIN
        SET @existing = (SELECT COUNT(*)
                         FROM person
                         WHERE user = OLD.user);
        IF @existing = 0
        THEN
          DELETE FROM acl_sid
          WHERE id = OLD.id;
        END IF;
      END;
    END IF;
  END;
//

CREATE TRIGGER `root_profile` AFTER INSERT ON organization FOR EACH ROW
  BEGIN
    INSERT INTO systemEntity (version, public) VALUES (1, 0);
    INSERT INTO acl_sid VALUES (LAST_INSERT_ID(), 1, LAST_INSERT_ID());
    UPDATE systemEntity
    SET owner = LAST_INSERT_ID()
    WHERE id = LAST_INSERT_ID();
    INSERT INTO entity (id) VALUES (LAST_INSERT_ID());
    INSERT INTO person (id, surname, givenName, convioSyncStatus, user, organization)
    VALUES (LAST_INSERT_ID(), 'root', 'root', 0, NEW.id, 2);
  END//
DELIMITER ;

-- delete old persons not complete
-- need to update create and drop trigger scripts
-- removed direct grants for ROOT; verify that this does not impact any ability
