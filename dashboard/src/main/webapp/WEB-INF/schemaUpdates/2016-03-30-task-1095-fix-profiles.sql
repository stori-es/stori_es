DROP PROCEDURE IF EXISTS grantProfileOnOrg;
DELIMITER //

CREATE PROCEDURE grantProfileOnOrg()
  BEGIN
    DECLARE orgIdX, profileIdX INT;
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        organization,
        id
      FROM profile
      WHERE user IS NULL;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO orgIdX, profileIdX;

      IF NOT done
      THEN
        INSERT IGNORE acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
        VALUES (profileIdX, 0, profileIdX, NULL, 0, FALSE);
        INSERT INTO acl_entry (acl_object_identity, sid, mask)
        VALUES (profileIdX, orgIdX, 300)
        ON DUPLICATE KEY UPDATE mask = VALUES(mask);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL grantProfileOnOrg();
DROP PROCEDURE grantProfileOnOrg;
