DROP PROCEDURE IF EXISTS createBodyDocumentForCollectionsQuestionnaires;

DELIMITER //

CREATE PROCEDURE createBodyDocumentForCollectionsQuestionnaires()
  BEGIN
    DECLARE id, owner, author INT;
    DECLARE title, permalinkX VARCHAR(255);
    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        c.id,
        e.title,
        se.owner,
        p.id,
        e.permalink
      FROM collection c
        JOIN entity e ON e.id = c.id
        JOIN systemEntity se ON se.id = c.id
        JOIN profile p ON p.organization = se.owner
      WHERE p.user = 0;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO id, title, owner, author, permalinkX;
      IF NOT done
      THEN
        IF permalinkX IS NULL
        THEN
          SET permalinkX = (SELECT SUBSTRING(MD5(RAND()) FROM 1 FOR 10));
        END IF;

        INSERT INTO systemEntity (version, public, owner)
        VALUES (1, TRUE, owner);
        INSERT INTO document (id, primaryAuthor, systemEntity, systemEntityRelation, permalink, locale)
        VALUES (LAST_INSERT_ID(), author, id, 'BODY', UPPER(permalinkX), 'en');
        INSERT INTO documentText (documentId, title, version, textType)
        VALUES (LAST_INSERT_ID(), title, 1, 'PLAIN');
      END IF;
      -- IF NOT done
    UNTIL done END REPEAT;
  END//

DELIMITER ;

CALL createBodyDocumentForCollectionsQuestionnaires();
DROP PROCEDURE createBodyDocumentForCollectionsQuestionnaires;
