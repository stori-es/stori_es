DROP PROCEDURE IF EXISTS restoreEmailContacts;

DELIMITER //
CREATE PROCEDURE restoreEmailContacts()
  BEGIN
    DECLARE authorX, idxX INT;
    DECLARE emailValue, optX VARCHAR(255);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT DISTINCT
        ansd.primaryAuthor,
        qc.opt,
        a.reportValue
      FROM answerSet anss
        JOIN document ansd ON ansd.id = anss.id
        JOIN questionnaire qs ON qs.id = anss.questionnaire
        JOIN document d ON d.systemEntity = qs.id AND d.systemEntityRelation = 'SURVEY'
        JOIN answer a ON anss.id = a.answerSet
        JOIN block_question q ON q.label = a.label AND q.document = d.id AND q.version = d.version
        JOIN question_contact qc ON q.document = qc.document AND qc.version = q.version AND qc.idx = q.idx
        LEFT JOIN contact c ON c.entityId = ansd.primaryAuthor AND c.value = a.reportValue
      WHERE c.entityId IS NULL AND (q.dataType = 'EMAIL' OR qc.type LIKE 'EMAIL%');

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO authorX, optX, emailValue;
      IF NOT done
      THEN
        SET @idxX =
        (SELECT COALESCE(MAX(idx), 0) + 1
         FROM contact c
         WHERE c.entityId = authorX);

        INSERT INTO contact (entityId, medium, type, value, idx, status)
        VALUES (authorX, 'EMAIL', optX, emailValue, @idxX, 'UNVERIFIED');
      END IF;
    UNTIL done END REPEAT;
    CLOSE cur;
  END //

DELIMITER ;

CALL restoreEmailContacts();
DROP PROCEDURE restoreEmailContacts;
