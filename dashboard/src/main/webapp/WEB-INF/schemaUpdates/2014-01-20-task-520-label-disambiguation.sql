DELIMITER //

-- Theortetically, this update is incomplete. Specifically, it doesn't
-- check to make sure that the labels it generates are actually unique
-- within the questionnaire. However, in practice it is sufficient.
CREATE PROCEDURE updateQuestionLabel()
  BEGIN
    DECLARE questionnaireX, idxX INT;
    DECLARE labelX VARCHAR(32);

    DECLARE done INT DEFAULT 0;
    -- We select all questions which share albel with a question in the
    -- same questionnaire.
    DECLARE cur CURSOR FOR
      SELECT
        q1.questionnaire,
        q1.idx,
        q1.label
      FROM question q1
        JOIN question q2
          ON q1.questionnaire = q2.questionnaire
             AND q1.idx > q2.idx
             AND q1.label = q2.label;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO questionnaireX, idxX, labelX;
      IF NOT done
      THEN
        SET @char_idx = (SELECT CHAR_LENGTH(idxX)
                         FROM dual);
        UPDATE question
        SET label = CONCAT(SUBSTR(labelX, 0, CHAR_LENGTH(labelX) - @char_idx), idxX)
        WHERE questionnaire = questionnaireX AND idx = idxX;
      END IF;
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL updateQuestionLabel();
DROP PROCEDURE updateQuestionLabel;
