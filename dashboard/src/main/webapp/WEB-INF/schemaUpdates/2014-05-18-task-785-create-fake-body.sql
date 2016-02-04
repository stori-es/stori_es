DROP PROCEDURE IF EXISTS createDocuments;
DELIMITER //
CREATE PROCEDURE createDocuments()
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE storyX, ownerX INT;
    DECLARE permalinkX VARCHAR(255);
    DECLARE idX INT;
    DECLARE cur CURSOR FOR SELECT
                             story,
                             owner,
                             permalink
                           FROM collection_story cs JOIN story s ON s.id = cs.story
                           WHERE (SELECT COUNT(*)
                                  FROM document d
                                  WHERE cs.story = d.systemEntity) = 0;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    REPEAT
      FETCH cur
      INTO storyX, ownerX, permalinkX;
      IF NOT done
      THEN
        INSERT INTO systemEntity (created, lastModified, owner, version) VALUES (NOW(), NOW(), ownerX, 0);
        SET @idX = LAST_INSERT_ID();
        INSERT INTO document VALUES (@idX, ownerX, NULL, UPPER(permalinkX), 1, storyX, 'BODY');
        INSERT INTO documentText VALUES (@idX, 0, 'PLAIN', 'This story was not submitted successfully.');
      END IF;
    UNTIL done END REPEAT;
    CLOSE cur;
  END//
DELIMITER ;
CALL createDocuments();
DROP PROCEDURE IF EXISTS createDocuments;

DELETE e, s FROM systemEntity e
  JOIN story s ON s.id = e.id
WHERE e.id IN
      (SELECT t.id
       FROM (SELECT s2.id
             FROM story s2
             WHERE (SELECT COUNT(*)
                    FROM document d
                    WHERE s2.id = d.systemEntity) = 0) t);
