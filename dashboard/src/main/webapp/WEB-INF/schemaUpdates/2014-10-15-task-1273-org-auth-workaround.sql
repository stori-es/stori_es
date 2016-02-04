DROP PROCEDURE IF EXISTS grantUserWriteOverOrg;

DELIMITER //

CREATE PROCEDURE grantUserWriteOverOrg()
  BEGIN
    DECLARE profileIdX, organizationIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        p.id,
        o.id
      FROM profile p
        JOIN organization o ON p.organization = o.id
        JOIN user u ON u.id = p.user
      WHERE p.id NOT IN
            (SELECT p2.id
             FROM profile p2
               JOIN organization o2 ON p2.organization = o2.id
               JOIN user u2 ON p2.user = u2.id
               JOIN acl_entry acl ON p2.id = acl.sid AND o2.id = acl.acl_object_identity
             WHERE acl.mask = 2);

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
        VALUES (organizationIdX, @ace_order, profileIdX, 2, 1, 0, 0);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL grantUserWriteOverOrg();
DROP PROCEDURE grantUserWriteOverOrg;
