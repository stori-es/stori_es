DELETE FROM acl_entry
WHERE mask = 32;

DELIMITER //

CREATE PROCEDURE authToRole()
  BEGIN
    DECLARE maskX, sidX, acl_object_identityX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        MAX(mask),
        sid,
        acl_object_identity
      FROM acl_entry
      WHERE mask IN (1, 2, 16)
      GROUP BY sid, acl_object_identity;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO maskX, sidX, acl_object_identityX;
      IF NOT done
      THEN
        IF maskX = 16
        THEN
          UPDATE acl_entry
          SET mask = 300
          WHERE sid = sidX AND acl_object_identity = acl_object_identityX AND mask = 16;
          DELETE FROM acl_entry
          WHERE sid = sidX AND acl_object_identity = acl_object_identityX AND mask IN (1, 2);
        ELSEIF maskX = 2
          THEN
            UPDATE acl_entry
            SET mask = 200
            WHERE sid = sidX AND acl_object_identity = acl_object_identityX AND mask = 2;
            DELETE FROM acl_entry
            WHERE sid = sidX AND acl_object_identity = acl_object_identityX AND mask IN (1);
        ELSE
          UPDATE acl_entry
          SET mask = 100
          WHERE sid = sidX AND acl_object_identity = acl_object_identityX AND mask = 1;
        END IF;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL authToRole();
DROP PROCEDURE authToRole;

-- There is no 'DROP IF EXISTS', so we have to do this four liner. The
-- index does not exist on Y.
SET @exist := (SELECT COUNT(*)
               FROM information_schema.statistics
               WHERE table_name = 'acl_entry' AND index_name = 'unique_object_identity_ace_order' AND
                     table_schema = database());
SET @sqlstmt := IF(@exist > 0, 'ALTER TABLE acl_entry DROP INDEX unique_object_identity_ace_order', 'SELECT 0');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;

ALTER TABLE acl_entry
DROP COLUMN id,
DROP COLUMN ace_order,
DROP COLUMN audit_success,
DROP COLUMN audit_failure,
DROP COLUMN granting;
-- this removes duplicates
SET SESSION old_alter_table = 1;
ALTER IGNORE TABLE acl_entry ADD UNIQUE INDEX unique_key(sid, mask, acl_object_identity);
-- notice the primary key does NOT reference the mask
ALTER TABLE acl_entry ADD PRIMARY KEY (sid, acl_object_identity);
-- now that we have the primary key, we drop the unique index
ALTER TABLE acl_entry DROP INDEX unique_key;
