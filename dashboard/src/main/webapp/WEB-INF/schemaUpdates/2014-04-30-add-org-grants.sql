DROP PROCEDURE IF EXISTS grantProfileReadOrg;

DELIMITER //

CREATE PROCEDURE grantProfileReadOrg()
  BEGIN
    DECLARE profileIdX, organizationIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        id,
        organization
      FROM profile;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO profileIdX, organizationIdX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT IFNULL(MAX(ace_order), -1)
                          FROM acl_entry
                          WHERE acl_object_identity = organizationIdX) + 1;
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
        VALUES (organizationIdX, @ace_order, profileIdX, 1, 1, 0, 0);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL grantProfileReadOrg();
DROP PROCEDURE grantProfileReadOrg;
