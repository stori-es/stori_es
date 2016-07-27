DROP PROCEDURE IF EXISTS removeStoryData;

DELIMITER //

CREATE PROCEDURE removeStoryData()
  BEGIN
    DECLARE storyIdX INT DEFAULT 796880;
    DECLARE profileIdX INT DEFAULT 0;
    DECLARE answerSetIdX INT DEFAULT 0;

    SELECT a.id
    FROM answerSet a
      JOIN document d ON a.id = d.id
    WHERE d.systemEntity = storyIdX
    INTO answerSetIdX;

    SELECT p.id
    FROM story s
      JOIN profile p ON p.id = s.owner
    WHERE s.id = storyIdX
    INTO profileIdX;

    DELETE FROM answer
    WHERE answerSet = answerSetIdX;
    DELETE FROM answerSet
    WHERE id = answerSetIdX;
    DELETE bc, bi
    FROM block b
      LEFT JOIN block_content bc ON b.document = bc.document AND b.version = bc.version AND b.idx = bc.idx
      LEFT JOIN block_image bi ON b.document = bi.document AND b.version = bi.version AND b.idx = bi.idx
      JOIN document d ON d.id = b.document
    WHERE d.systemEntity = storyIdX;
    DELETE b
    FROM block b
      JOIN document d ON d.id = b.document
    WHERE d.systemEntity = storyIdX;
    DELETE d, se
    FROM document d
      JOIN systemEntity se ON d.id = se.id
    WHERE d.systemEntity = storyIdX;

    DELETE FROM tag
    WHERE systemEntity = storyIdX;
    DELETE FROM address
    WHERE entity = storyIdX;
    DELETE s.*, se.*
    FROM story s
      JOIN systemEntity se ON s.id = se.id
    WHERE s.id = storyIdX;

    DELETE FROM contact
    WHERE entityId = profileIdX;
    DELETE FROM address
    WHERE entity = profileIdX;
    DELETE FROM acl_entry
    WHERE acl_object_identity = profileIdX OR sid = profileIdX;
    DELETE FROM acl_object_identity
    WHERE id = profileIdX;
    DELETE FROM acl_sid
    WHERE sid = profileIdX AND sid != 0;
    DELETE FROM verification_nonce
    WHERE profile = profileIdX;
    DELETE FROM profile
    WHERE id = profileIdX;
    DELETE FROM entity
    WHERE id = profileIdX;
    DELETE FROM systemEntity
    WHERE id = profileIdX;
  END//

CALL removeStoryData();
DROP PROCEDURE IF EXISTS removeStoryData;
DELIMITER ;
