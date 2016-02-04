-- Grants Organizations READ, WRITE, and ADMIN rights over any Person
-- authoring a Story associated to a Collection owned by the
-- Organization. The app logic, as of 2013-01-15, should make these
-- grants to the Organization to whom the author submits their first
-- story and READ writes to other Organizations after that point. This
-- script does not make that distinction, and grants the full boat to
-- all Orgs. In future, it will also be possible for storytellers to
-- manage their own authorizaitons and they may choose to revoke
-- privileges. These factors should be considered before executing
-- this script.

DELIMITER //

CREATE PROCEDURE updatePersonAccessPrivs(IN maskX INT)
BEGIN
  DECLARE personIdX, orgIdX INT;

  DECLARE done INT DEFAULT 0;
  DECLARE cur CURSOR FOR
    SELECT s.owner, c.organization
      FROM collection c
      JOIN collection_story cs ON c.id=cs.collection
      JOIN story s on s.id=cs.story
      LEFT JOIN acl_entry acl 
        ON s.owner=acl.acl_object_identity AND mask=maskX
	  AND acl.sid IN (SELECT id FROM organization)
      WHERE acl.id IS NULL AND s.owner IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;

  OPEN cur;
  REPEAT
    FETCH cur INTO personIdX, orgIdX;
    IF NOT done THEN
      SET @ace_order=(SELECT COALESCE(MAX(ace_order),-1) + 1 FROM acl_entry WHERE acl_object_identity=personIdX);
      INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (personIdX, @ace_order, orgIdX, maskX, TRUE, FALSE, FALSE);
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL updatePersonAccessPrivs(1); -- READ
CALL updatePersonAccessPrivs(2); -- WRITE
CALL updatePersonAccessPrivs(16); -- ADMIN

DROP PROCEDURE updatePersonAccessPrivs;