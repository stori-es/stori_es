-- There are two bare collections, no questions, no stories. This clears them.
DELETE c
FROM collection c
  LEFT JOIN document d ON d.systemEntity = c.id AND d.systemEntityRelation = 'BODY'
  LEFT JOIN systemEntity de ON de.id = d.id AND de.version = d.version
WHERE d.id IS NULL;

-- document has blocks
-- block_content isa block
-- block_link isa block
-- block_question isa block
-- block_image isa block

RENAME TABLE content TO block_content;
ALTER TABLE block_content DROP FOREIGN KEY fk_contentL10n_formElement;
ALTER TABLE block_content CHANGE questionnaire document INT;
ALTER TABLE block_content ADD COLUMN version INT;
ALTER TABLE block_content DROP PRIMARY KEY;
RENAME TABLE documentContent TO block_link;
ALTER TABLE block_link DROP FOREIGN KEY fk_documentContent_formElement;
ALTER TABLE block_link ADD COLUMN version INT;
ALTER TABLE block_link CHANGE questionnaire document INT;
ALTER TABLE block_link DROP PRIMARY KEY;
RENAME TABLE imageContent TO block_image;
ALTER TABLE block_image DROP FOREIGN KEY fk_imageContent_formElement;
ALTER TABLE block_image ADD COLUMN version INT;
ALTER TABLE block_image CHANGE questionnaire document INT;
ALTER TABLE block_image DROP PRIMARY KEY;
RENAME TABLE question TO block_question;
ALTER TABLE block_question DROP FOREIGN KEY fk_question_formElement;
ALTER TABLE block_question ADD COLUMN version INT;
ALTER TABLE block_question DROP INDEX uniq_questionnaire_label;
ALTER TABLE block_question CHANGE questionnaire questionnaire INT;
ALTER TABLE block_question ADD CONSTRAINT uniq_questionnaire_label UNIQUE (questionnaire, version, label);
ALTER TABLE submitBlock DROP FOREIGN KEY submitBlock_ibfk_1;
RENAME TABLE submitBlock TO block_submit;
ALTER TABLE block_submit CHANGE questionnaire document INT;
ALTER TABLE block_submit ADD COLUMN version INT;
ALTER TABLE block_submit DROP PRIMARY KEY;
ALTER TABLE block DROP FOREIGN KEY fk_formElement_collection;
ALTER TABLE ratingBlock DROP FOREIGN KEY ratingBlock_ibfk_1;
ALTER TABLE ratingBlock DROP PRIMARY KEY;
ALTER TABLE block DROP PRIMARY KEY;
ALTER TABLE block CHANGE questionnaire document INT;
ALTER TABLE block CHANGE formType documentType VARCHAR(16);
ALTER TABLE block MODIFY COLUMN documentType VARCHAR(16) NOT NULL;
-- We've updated the enum type to be clearer; 'TEXT' and 'CONTENT' can
-- be confusing; 'TEXT_INPUT' is a bit clearer.
UPDATE block
SET documentType = 'TEXT_INPUT'
WHERE documentType = 'TEXT';
UPDATE block
SET documentType = 'SUBHEADER'
WHERE documentType = 'SUB-HEADER';

UPDATE block
SET standardMeaning = 'FIRST_NAME'
WHERE standardMeaning = 'FIRST NAME';
UPDATE block
SET standardMeaning = 'LAST_NAME'
WHERE standardMeaning = 'LAST NAME';
UPDATE block
SET standardMeaning = 'EMAIL_WORK'
WHERE standardMeaning = 'EMAIL WORK';
UPDATE block
SET standardMeaning = 'EMAIL_OTHER'
WHERE standardMeaning = 'EMAIL OTHER';
UPDATE block
SET standardMeaning = 'STREET_ADDRESS_1'
WHERE standardMeaning = 'STREET ADDRESS 1';
UPDATE block
SET standardMeaning = 'ZIP_CODE'
WHERE standardMeaning = 'ZIP CODE';
UPDATE block
SET standardMeaning = 'PHONE_MOBILE'
WHERE standardMeaning = 'PHONE MOBILE';
UPDATE block
SET standardMeaning = 'PHONE_WORK'
WHERE standardMeaning = 'PHONE WORK';
UPDATE block
SET standardMeaning = 'PHONE_OTHER'
WHERE standardMeaning = 'PHONE OTHER';
UPDATE block
SET standardMeaning = 'MAILING_OPT_IN'
WHERE standardMeaning = 'MAILING OPT IN';
UPDATE block
SET standardMeaning = 'PREFERRED_EMAIL_FORMAT'
WHERE standardMeaning = 'PREFERRED EMAIL FORMAT';
UPDATE block
SET standardMeaning = 'STORY_ASK'
WHERE standardMeaning = 'STORY ASK';
UPDATE block
SET standardMeaning = 'CUSTOM_PERMISSIONS'
WHERE standardMeaning = 'CUSTOM PERMISSIONS';
UPDATE block
SET standardMeaning = 'UPDATES_OPT_IN'
WHERE standardMeaning = 'UPDATES OPT-IN';
UPDATE block
SET standardMeaning = 'STORY_TITLE'
WHERE standardMeaning = 'STORY TITLE';
ALTER TABLE block ADD COLUMN version INT;
ALTER TABLE contactContent DROP FOREIGN KEY fk_contactContent_question;
RENAME TABLE contactContent TO question_contact;
ALTER TABLE question_contact ADD COLUMN version INT;
ALTER TABLE question_contact CHANGE questionnaire document INT;
ALTER TABLE question_contact DROP PRIMARY KEY;
RENAME TABLE questionOptions TO question_options;
ALTER TABLE question_options DROP FOREIGN KEY fk_questionOptions_question;
ALTER TABLE question_options CHANGE questionnaire document INT;
ALTER TABLE question_options ADD COLUMN version INT;
ALTER TABLE question_options DROP PRIMARY KEY;
UPDATE question_options
SET version = 0;
ALTER TABLE block_question CHANGE questionnaire document INT;
ALTER TABLE block_question DROP PRIMARY KEY;
RENAME TABLE ratingBlock TO block_rating;
ALTER TABLE block_rating ADD COLUMN version INT;
UPDATE block_rating
SET version = 0;
ALTER TABLE block_rating CHANGE questionnaire document INT;

DROP PROCEDURE IF EXISTS refactorDocuments;

DELIMITER //

CREATE PROCEDURE refactorDocuments()
  BEGIN
    DECLARE collectionIdX INT;
    DECLARE isQ BOOL;
    DECLARE docId INT;
    DECLARE docVersion INT;
    DECLARE done INT DEFAULT 0;

    DECLARE cur CURSOR FOR
      SELECT DISTINCT
        (c.id),
        EXISTS(SELECT 1
               FROM questionnaire q
               WHERE q.id = c.id)
      FROM collection c
        JOIN block b ON c.id = b.document
        LEFT JOIN questionnaire q ON q.id = c.id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO collectionIdX, isQ;
      IF NOT done
      THEN

        SET @ownerX = (SELECT owner
                       FROM systemEntity
                       WHERE id = collectionIdX);
        SET @publicX = (SELECT public
                        FROM systemEntity
                        WHERE id = collectionIdX);

        INSERT INTO systemEntity (version, public, owner) VALUES (0, @publicX, @ownerX);
        -- The primaryAuthor on the document has to be set, but there
        -- really is no good answer. The data simply isn't present. As a
        -- workaround, we use gregg for CU and pick an admin for
        -- everything else.
        SET @author = 762401;
        IF @owner != 2
        THEN
          SET @owner = (SELECT p.id
                        FROM profile p
                          JOIN acl_entry acl ON p.id = acl.sid
                          JOIN organization o ON o.id = acl.acl_object_identity
                        WHERE mask = 300 AND o.id = 236145
                        LIMIT 1);
        END IF;

        IF isQ = TRUE
        THEN
          INSERT INTO document (id, primaryAuthor, permalink, systemEntity, systemEntityRelation, locale, version)
          VALUES (LAST_INSERT_ID(), @author, SUBSTRING(MD5(LAST_INSERT_ID()), 0, 8), collectionIdX, 'SURVEY', 'en', 0);
          SET docId = LAST_INSERT_ID();
          SET docVersion = 0;
        ELSE
          SET docId = (SELECT DISTINCT (d.id)
                       FROM document d
                       WHERE d.systemEntity = collectionIdX AND systemEntityRelation = 'BODY');
          SET docVersion = (SELECT MAX(d.version)
                            FROM document d
                            WHERE d.systemEntity = collectionIdX AND systemEntityRelation = 'BODY'
                            GROUP BY d.systemEntity);
        END IF;

        UPDATE block
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_content
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_image
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_link
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_rating
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_submit
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE block_question
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE question_contact
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
        UPDATE question_options
        SET document = docId, version = docVersion
        WHERE document = collectionIdX;
      END IF;
    UNTIL done END REPEAT;

  END//

DELIMITER ;

CALL refactorDocuments();
DROP PROCEDURE refactorDocuments;

ALTER TABLE block ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block ADD CONSTRAINT fk_block_document FOREIGN KEY (document, version) REFERENCES document (id, version);
ALTER TABLE block_question ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_question ADD CONSTRAINT fk_block_question_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE block_content ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_content ADD CONSTRAINT fk_block_content_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE question_options ADD PRIMARY KEY (document, version, questionIdx, idx);
ALTER TABLE question_options ADD CONSTRAINT fk_question_options_question FOREIGN KEY (document, version, questionIdx) REFERENCES block_question (document, version, idx);
ALTER TABLE block_submit ADD CONSTRAINT fk_block_submit_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE block_submit ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_link ADD CONSTRAINT fk_block_link_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE block_link ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_image ADD CONSTRAINT fk_block_image_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE block_image ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_rating ADD PRIMARY KEY (document, version, idx);
ALTER TABLE block_rating ADD CONSTRAINT fk_block_rating_block FOREIGN KEY (document, version, idx) REFERENCES block (document, version, idx);
ALTER TABLE question_contact ADD PRIMARY KEY (document, version, idx, type, opt);
ALTER TABLE question_contact ADD CONSTRAINT fk_question_contact_question FOREIGN KEY (document, version, idx) REFERENCES block_question (document, version, idx);

-- Might as well fix this misspelling.
UPDATE document
SET systemEntityRelation = 'ATTACHMENT'
WHERE systemEntityRelation = 'ATTACHEMENT';

ALTER TABLE block_content ADD COLUMN textType VARCHAR(16);

DROP PROCEDURE IF EXISTS refactorText;

DELIMITER //

CREATE PROCEDURE refactorText()
  BEGIN
    DECLARE documentIdX, versionX INT;
    DECLARE textTypeX VARCHAR(16);
    DECLARE textX TEXT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        documentId,
        textType,
        text,
        version
      FROM documentText;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO documentIdX, textTypeX, textX, versionX;
      IF NOT done
      THEN
        SET @idxX =
        (SELECT COALESCE(MAX(idx), 0) + 1
         FROM block b
         WHERE b.document = documentIdX AND b.version = versionX);
        INSERT INTO block (document, idx, documentType, standardMeaning, version)
        VALUES (documentIdX, @idxX, 'CONTENT', NULL, versionX);
        INSERT INTO block_content (document, idx, content, textType, version)
        VALUES (documentIdX, @idxX, textX, textTypeX, versionX);
      END IF;

    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL refactorText();

DROP PROCEDURE refactorText;

DROP TABLE documentText;
