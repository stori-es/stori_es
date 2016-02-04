INSERT INTO collection_story (collection, story)
  SELECT c.id, s.id
    FROM answerSet a 
    JOIN questionnaire q ON a.questionnaire=q.id
    JOIN collection c ON c.id=q.collection
    JOIN document d ON d.id=a.id 
    JOIN story s ON s.id=d.systemEntity
    LEFT JOIN collection_story cs ON cs.story=d.systemEntity
    WHERE cs.story IS NULL;

DELIMITER //

CREATE PROCEDURE updateStoryAuths()
BEGIN
  DECLARE organizationIdX, storyIdX, collectionIdX INT;
  
  DECLARE done INT DEFAULT 0;
  -- select all the user who don't currently have the specified grant
  DECLARE cur CURSOR FOR
    SELECT c.organization, s.id, cs.collection
      FROM story s
      JOIN collection_story cs ON s.id=cs.story 
      JOIN collection c ON c.id=cs.collection
      LEFT JOIN acl_entry acl ON s.id=acl.acl_object_identity AND acl.sid!=0
      WHERE acl.id IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;
  
  REPEAT
    FETCH cur INTO organizationIdX, storyIdX, collectionIdX;
    IF NOT done THEN
      -- set to one more than max ace_order or 0 if no ace_order found
      SET @ace_order= 
        (SELECT COALESCE(MAX(ace_order),-1) + 1 FROM acl_entry WHERE acl_object_identity=storyIdX);
      -- now actually make the grant
      INSERT INTO acl_entry
        (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
      	VALUES (storyIdX, @ace_order, organizationIdX, 1, 1, 0, 0);
      INSERT INTO acl_entry
        (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
      	VALUES (storyIdX, @ace_order + 1, organizationIdX, 2, 1, 0, 0);
      INSERT INTO acl_entry
        (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
      	VALUES (storyIdX, @ace_order + 2, organizationIdX, 16, 1, 0, 0);
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

CALL updateStoryAuths();
DROP PROCEDURE updateStoryAuths;
