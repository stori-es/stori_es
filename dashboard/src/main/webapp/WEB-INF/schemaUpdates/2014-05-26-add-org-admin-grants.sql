DROP PROCEDURE IF EXISTS grantProfileOverCollections;

DELIMITER //

CREATE PROCEDURE grantProfileOverCollections()
  BEGIN
    DECLARE profileIdX, collectionIdX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        p.id,
        c.id
      FROM profile p
        JOIN user u ON p.user = u.id
        JOIN systemEntity ce ON ce.owner = p.organization
        JOIN collection c ON ce.id = c.id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO profileIdX, collectionIdX;
      IF NOT done
      THEN
        SET @ace_order = (SELECT IFNULL(MAX(ace_order), -1)
                          FROM acl_entry
                          WHERE acl_object_identity = collectionIdX) + 1;
        INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
        VALUES (collectionIdX, @ace_order, profileIdX, 1, 1, 0, 0),
          (collectionIdX, @ace_order + 1, profileIdX, 2, 1, 0, 0),
          (collectionIdX, @ace_order + 2, profileIdX, 16, 1, 0, 0);
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

INSERT INTO acl_object_identity
(id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
  (SELECT
     c.id,
     0,
     c.id,
     NULL,
     0,
     0
   FROM collection c LEFT JOIN acl_object_identity a ON c.id = a.id
   WHERE a.id IS NULL);

CALL grantProfileOverCollections();
DROP PROCEDURE grantProfileOverCollections;
