DROP PROCEDURE IF EXISTS setupWhiteList;
DROP PROCEDURE IF EXISTS grantProfileOnCollection;

DELIMITER //

CREATE PROCEDURE setupWhiteList()
  BEGIN
    DECLARE profileIdX, maskX, orgIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        p.id AS profileId,
        MAX(acl.mask),
        o.id AS orgId
      FROM acl_entry acl
        JOIN profile p ON p.id = acl.sid
        JOIN organization o ON o.id = acl.acl_object_identity
        JOIN user u ON p.user = u.id
      GROUP BY profileId, orgId;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO profileIdX, maskX, orgIdX;
      IF NOT done
      THEN
        IF maskX = 1
        THEN
          SET @role = 'READER';
        ELSEIF maskX = 2
          THEN
            SET @role = 'CURATOR';
        ELSEIF maskX = 16 OR maskX = 32
          THEN
            SET @role = 'ADMIN';
        END IF;
        -- determine @role
        INSERT INTO white_list VALUES (orgIdX, @role, @role, profileIdX);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

CREATE PROCEDURE grantProfileOnCollection()
  BEGIN
    DECLARE profileIdX, collectionIdX INT;
    DECLARE roleX VARCHAR(16);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        wl.member,
        wl.default_role,
        c.id
      FROM white_list wl
        JOIN systemEntity ce ON wl.organization = ce.owner
        JOIN collection c ON ce.id = c.id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO profileIdX, roleX, collectionIdX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT IFNULL(MAX(ace_order), -1)
                          FROM acl_entry
                          WHERE acl_object_identity = collectionIdX) + 1;
        IF roleX = 'READER'
        THEN
          INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
          VALUES (collectionIdX, @ace_order, profileIdX, 1, 1, 0, 0);
        ELSEIF roleX = 'CURATOR'
          THEN
            INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
            VALUES (collectionIdX, @ace_order, profileIdX, 1, 1, 0, 0),
              (collectionIdX, (@ace_order + 1), profileIdX, 1, 2, 0, 0);
        ELSEIF roleX = 'ADMIN'
          THEN
            INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
            VALUES (collectionIdX, @ace_order, profileIdX, 1, 1, 0, 0),
              (collectionIdX, (@ace_order + 1), profileIdX, 1, 2, 0, 0),
              (collectionIdX, (@ace_order + 2), profileIdX, 1, 16, 0, 0);
        END IF;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CREATE TABLE white_list (
  organization INT(11)     NOT NULL,
  default_role VARCHAR(16) NOT NULL,
  max_role     VARCHAR(16) NOT NULL,
  member       INT(11)     NOT NULL,
  PRIMARY KEY (organization, member),
  CONSTRAINT fk_white_list_organization FOREIGN KEY (organization) REFERENCES organization (id),
  CONSTRAINT fk_white_list_member_entity FOREIGN KEY (member) REFERENCES entity (id)
);

CREATE TABLE roles (
  grantor INT(11)     NOT NULL,
  role    VARCHAR(16) NOT NULL,
  grantee INT(11)     NOT NULL,
  PRIMARY KEY (grantor, grantee),
  CONSTRAINT fk_roles_grantor_entity FOREIGN KEY (grantor) REFERENCES entity (id),
  CONSTRAINT fk_roles_grantee_entity FOREIGN KEY (grantee) REFERENCES entity (id)
);

CALL setupWhiteList();
DROP PROCEDURE setupWhiteList;
CALL grantProfileOnCollection();
DROP PROCEDURE grantProfileOnCollection;
