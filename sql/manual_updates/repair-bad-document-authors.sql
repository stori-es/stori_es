-- Repairs BODY document ‘primaryAuthor’ fields erroneously set to 0
-- by matching ANSWER_SET associated to the Story. The script was
-- originally developed to fix a specific issues wherein BODY authors
-- were not recorded but ANSWER_SET documents were. The script may be
-- non-useful and possible make incorrect changes in other
-- circumstances.

DELIMITER //

CREATE PROCEDURE repairBadDocumentAuthors()
BEGIN
  DECLARE storyIdX, personIdX INT;

  DECLARE done INT DEFAULT 0;
  DECLARE cur CURSOR FOR
    SELECT da.systemEntity, da.primaryAuthor
      FROM document db 
      JOIN document da ON db.systemEntity=da.systemEntity
        AND da.systemEntityRelation='ANSWER_SET'
      WHERE db.primaryAuthor=0 AND db.systemEntityRelation='BODY';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;

  OPEN cur;
  REPEAT
    FETCH cur INTO storyIdX, personIdX;
    IF NOT done THEN
      UPDATE document SET primaryAuthor=personIdX WHERE systemEntity=storyIdX AND systemEntityRelation='BODY';
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //
DELIMITER ;

CALL repairBadDocumentAuthors();

DROP PROCEDURE repairBadDocumentAuthors;