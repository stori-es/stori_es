DROP PROCEDURE IF EXISTS updateFormLabels;

DELIMITER //

CREATE PROCEDURE updateFormLabels()
  BEGIN
    DECLARE questionnaireX, idxX INT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        questionnaire,
        idx
      FROM question
      WHERE label IN ('EMAIL', 'EMAIL OTHER', 'EMAIL WORK', 'PHONE', 'PHONE MOBILE', 'PHONE WORK', 'PHONE OTHER');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO questionnaireX, idxX;
      IF NOT done
      THEN
        UPDATE question
        SET label = CONCAT('contact', idx)
        WHERE questionnaire = questionnaireX AND idx = idxX;
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL updateFormLabels();
DROP PROCEDURE updateFormLabels;

ALTER TABLE question ADD CONSTRAINT uniq_questionnaire_label UNIQUE (questionnaire, label);
