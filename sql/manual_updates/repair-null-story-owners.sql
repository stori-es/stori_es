-- Repairs null story.owner fields by pulling information from the
-- related documnt.primaryAuthor field. As of 2013-01-15, this is safe
-- as there is no practical way to create BODY and ANSWER_SET
-- documents with different authors, but such a state is possible
-- according to the design. These limitations should be considered
-- before executing this script.

DELIMITER //

CREATE PROCEDURE repairNullStoryOwners()
BEGIN
  DECLARE storyIdX INT;

  DECLARE done INT DEFAULT 0;
  DECLARE cur CURSOR FOR
    SELECT id FROM story WHERE owner IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;

  OPEN cur;
  REPEAT
    FETCH cur INTO storyIdX;
    IF NOT done THEN
      SET @owner=(SELECT primaryAuthor FROM document WHERE systemEntity=storyIdX AND primaryAuthor>0 LIMIT 1);
      UPDATE story SET owner=@owner WHERE id=storyIdX;
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //
DELIMITER ;

CALL repairNullStoryOwners();

DROP PROCEDURE repairNullStoryOwners;