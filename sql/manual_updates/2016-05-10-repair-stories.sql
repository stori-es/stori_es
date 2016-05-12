UPDATE block b
  JOIN block_question bq ON b.document=bq.document AND b.version=bq.version AND b.idx=bq.idx
  SET b.blockType = 'STORY_ASK_PLAIN'
  WHERE bq.text LIKE '%tell%story%' AND b.blockType='TEXT_AREA';
UPDATE block b
  JOIN block_question bq ON b.document=bq.document AND b.version=bq.version AND b.idx=bq.idx
  SET b.blockType = 'STORY_ASK_RICH'
  WHERE bq.text LIKE '%tell%story%' AND b.blockType='RICH_TEXT_AREA';

DROP PROCEDURE IF EXISTS rebuildStoryBody;

DELIMITER //

CREATE PROCEDURE rebuildStoryBody()
BEGIN
  DECLARE storyTextX TEXT;
  DECLARE qbBlockTypeX VARCHAR(32);
  DECLARE ownerX, storyIdX INT;
  DECLARE titleX VARCHAR(255);
  
  DECLARE done INT DEFAULT 0;
  -- select all the user who don't currently have the specified grant
  DECLARE cur CURSOR FOR
    SELECT a_a.reportValue, q_b.blockType, a_se.owner, a_s.id, a_bt.reportValue
    FROM block q_b 
      JOIN block_question q_bq ON q_b.document=q_bq.document AND q_b.idx=q_bq.idx AND q_b.version=q_bq.version 
      JOIN document q_d ON q_b.document=q_d.id AND q_b.version=q_d.version 
      JOIN answerSet aset ON aset.questionnaire=q_d.systemEntity 
      JOIN document a_d ON aset.id=a_d.id 
      JOIN answer a_a ON a_a.answerSet=aset.id AND a_a.label=q_bq.label
      JOIN story a_s ON a_d.systemEntity=a_s.id
      JOIN systemEntity a_se ON a_s.id=a_se.id
      LEFT JOIN block q_b2 ON q_b.document=q_b2.document AND q_b.idx=q_b2.idx AND q_b.version<q_b2.version 
      LEFT JOIN document s_d ON s_d.systemEntity=a_d.systemEntity AND s_d.systemEntityRelation='BODY'
      LEFT JOIN block q_bt ON q_bt.document=q_b.document AND q_bt.version=q_b.version AND q_bt.blockType='STORY_TITLE'
      LEFT JOIN block_question q_qbt ON q_bt.document=q_qbt.document AND q_bt.version=q_qbt.version ANd q_bt.idx=q_qbt.idx
      LEFT JOIN answer a_bt ON q_qbt.label=a_bt.label AND a_bt.answerSet=aset.id
    WHERE q_b.blockType IN ('STORY_ASK_RICH', 'STORY_ASK_PLAIN')
      AND q_b2.document IS NULL -- block version limit
      AND s_d.id IS NULL;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;
  
  REPEAT
    FETCH cur INTO storyTextX, qbBlockTypeX, ownerX, storyIdX, titleX;
    IF NOT done THEN
      IF qbBlockTypeX = 'STORY_ASK_PLAIN' THEN
        SET @textType = 'PLAIN';
      ELSE
        SET @textType = 'HTML';
      END IF;

      INSERT INTO systemEntity (version, public, owner, creator)
	VALUES (0, 0, NULL, NULL);
      SET @sDocId=LAST_INSERT_ID();
      INSERT INTO document (id, primaryAuthor, permalink, systemEntity, systemEntityRelation, locale, version, title, summary)
	VALUES (@sDocId, ownerX, UUID(), storyIdX, 'BODY', 'en', 1, titleX, NULL);
      INSERT INTO block (document, version, idx, blockType)
        VALUES (@sDocId, 1, 1, 'CONTENT');
      INSERT INTO block_content (document, version, idx, content, textType)
        VALUES (@sDocId, 1, 1, storyTextX, @textType);

    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL rebuildStoryBody();
DROP PROCEDURE rebuildStoryBody;

DROP PROCEDURE IF EXISTS rebuildDocBlocks;

DELIMITER //

CREATE PROCEDURE rebuildDocBlocks()
BEGIN
  DECLARE bodyDocIdX, bodyDocVersion, answerDocIdX INT;
  
  DECLARE done INT DEFAULT 0;
  -- select all the user who don't currently have the specified grant
  DECLARE cur CURSOR FOR
    SELECT bd.id, bd.version, ad.id
      FROM document bd
        JOIN story s ON s.id=bd.systemEntity AND bd.systemEntityRelation='BODY'
	JOIN document ad ON s.id=ad.systemEntity AND ad.systemEntityRelation='ANSWER_SET'
        LEFT JOIN block b ON bd.id=b.document
      WHERE b.document IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;
  
  REPEAT
    FETCH cur INTO bodyDocIdX, bodyDocVersion, answerDocIdX;
    IF NOT done THEN

      SET @storyText=(SELECT a.reportValue
                        FROM document ad
			  JOIN answerSet aSet ON ad.id=aSet.id
			  JOIN answer a ON a.answerSet=ad.id
			  JOIN questionnaire q ON aSet.questionnaire=q.id
			  JOIN document qd ON qd.systemEntity=q.id AND qd.systemEntityRelation='SURVEY'
			  JOIN block qb ON qb.document=qd.id ANd qb.blockType IN ('STORY_ASK_PLAIN', 'STORY_ASK_RICH')
			  JOIN block_question qbb ON qb.document=qbb.document AND qb.version=qbb.version AND qb.idx=qbb.idx AND a.label=qbb.label
			WHERE ad.id=answerDocIdX AND a.reportValue IS NOT NULL
			LIMIT 1);
      IF @storyText IS NOT NULL THEN
        SET @blockType=(SELECT qb.blockType
                          FROM document ad
			    JOIN answerSet aSet ON ad.id=aSet.id
			    JOIN answer a ON a.answerSet=ad.id
			    JOIN questionnaire q ON aSet.questionnaire=q.id
			    JOIN document qd ON qd.systemEntity=q.id AND qd.systemEntityRelation='SURVEY'
			    JOIN block qb ON qb.document=qd.id ANd qb.blockType IN ('STORY_ASK_PLAIN', 'STORY_ASK_RICH')
			    JOIN block_question qbb ON qb.document=qbb.document AND qb.version=qbb.version AND qb.idx=qbb.idx AND a.label=qbb.label
			  WHERE ad.id=answerDocIdX AND a.reportValue IS NOT NULL
			  LIMIT 1);

        IF @blockType = 'STORY_ASK_PLAIN' THEN
          SET @textType = 'PLAIN';
        ELSE
          SET @textType = 'HTML';
        END IF;

        INSERT INTO block (document, version, idx, blockType)
          VALUES (bodyDocIdX, bodyDocVersion, 1, 'CONTENT');
        INSERT INTO block_content (document, version, idx, content, textType)
          VALUES (bodyDocIdX, bodyDocVersion, 1, @storyText, @textType);

        SELECT bodyDocIdX FROM dual;

      END IF;
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL rebuildDocBlocks();
DROP PROCEDURE rebuildDocBlocks;

