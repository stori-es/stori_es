CREATE TABLE submitBlock (
  questionnaire INT NOT NULL,
  idx           INT NOT NULL,
  prompt        VARCHAR(250),
  size          VARCHAR(16),
  position      VARCHAR(16),
  PRIMARY KEY (questionnaire, idx),
  FOREIGN KEY (questionnaire, idx) REFERENCES block (questionnaire, idx)
);

DELIMITER //
CREATE PROCEDURE createSubmitBlocks()
  BEGIN
    DECLARE questionnaireX, idxX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT q.id
      FROM questionnaire q;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO questionnaireX;
      IF NOT done
      THEN
        SET @idxX =
        (SELECT COALESCE(MAX(idx), 0) + 1
         FROM block
         WHERE questionnaire = questionnaireX);
        INSERT INTO block (questionnaire, idx, formType)
        VALUES (questionnaireX, @idxX, 'SUBMIT');
        INSERT INTO submitBlock (questionnaire, idx, prompt, size, position)
        VALUES (questionnaireX, @idxX, 'SUBMIT', 'SMALL', 'CENTER');
      END IF;
    UNTIL done END REPEAT;
    CLOSE cur;
  END //

DELIMITER ;

CALL createSubmitBlocks();

DROP PROCEDURE createSubmitBlocks;
