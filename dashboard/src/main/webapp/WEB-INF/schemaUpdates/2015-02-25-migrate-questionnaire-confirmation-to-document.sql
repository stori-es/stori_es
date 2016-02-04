ALTER TABLE questionnaire
ADD confirmation2 INT;

DROP PROCEDURE IF EXISTS confirmationToDocument;
DELIMITER //
CREATE PROCEDURE confirmationToDocument()
  BEGIN
    DECLARE done INT DEFAULT FALSE;

    DECLARE qId, primAuth, sysEnt, owner, public INT;
    DECLARE conf TEXT;
    DECLARE locale VARCHAR(8);

    DECLARE cur CURSOR FOR
      SELECT
        q.id,
        d.primaryAuthor,
        d.systemEntity,
        d.locale,
        s.owner,
        s.public,
        q.confirmation
      FROM questionnaire q
        LEFT JOIN document d
          ON q.id = d.systemEntity
        LEFT JOIN systemEntity s
          ON q.id = s.id
      WHERE d.systemEntityRelation = 'BODY'
      GROUP BY d.systemEntity;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    OPEN cur;

    read_loop: LOOP
      FETCH cur
      INTO qId, primAuth, sysEnt, locale, owner, public, conf;
      IF done
      THEN
        LEAVE read_loop;
      END IF;

      INSERT INTO systemEntity (version, public, owner)
      VALUES (1, public, owner);

      INSERT INTO document (id, primaryAuthor, permalink, systemEntity, systemEntityRelation, locale, version, title)
      VALUES (LAST_INSERT_ID(), primAuth, '', sysEnt, 'CONTENT', locale, 1, 'Confirmation');

      INSERT INTO documentText (documentId, version, textType, text)
      VALUES (LAST_INSERT_ID(), 1, 'HTML', conf);

      UPDATE questionnaire
      SET confirmation2 = LAST_INSERT_ID()
      WHERE id = qId;
    END LOOP;

    CLOSE cur;

    ALTER TABLE questionnaire
    DROP COLUMN confirmation,
    CHANGE confirmation2 confirmation INT,
    ADD CONSTRAINT fk_questionnaire_document FOREIGN KEY (confirmation) REFERENCES document (id);
  END//

DELIMITER ;
CALL confirmationToDocument();
DROP PROCEDURE confirmationToDocument;
