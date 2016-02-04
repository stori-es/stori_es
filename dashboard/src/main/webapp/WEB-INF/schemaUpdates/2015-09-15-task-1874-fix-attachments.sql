DROP PROCEDURE IF EXISTS fixAttachmentBlocks;

DELIMITER //
CREATE PROCEDURE fixAttachmentBlocks()
  BEGIN
    DECLARE docId, version, ignored INT;
    DECLARE permalink, title VARCHAR(256);

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
      SELECT
        d.id,
        d.permalink,
        d.version,
        d.title,
        b.document
      FROM document d
        LEFT JOIN block b ON b.document = d.id AND b.version = d.version
        LEFT JOIN block_content bc ON bc.document = d.id AND bc.version = d.version AND bc.idx = b.idx
      WHERE d.systemEntityRelation = 'ATTACHMENT' AND (b.document IS NULL OR d.title IS NULL);

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN cur;

    REPEAT
      FETCH cur
      INTO docId, permalink, version, title, ignored;

      IF title IS NULL
      THEN
        SET title = permalink;
      END IF;

      IF NOT done
      THEN
        UPDATE document d
        SET title=(SELECT title)
        WHERE d.id=(SELECT docId) AND d.version=(SELECT version);

        INSERT INTO block (document, idx, documentType, version)
        VALUES (docId, 0, 'CONTENT', version);
        INSERT INTO block_content (document, idx, content, version)
        VALUES (docId, 0, title, version);
      END IF;
    UNTIL done END REPEAT;
    CLOSE cur;
  END //

DELIMITER ;

CALL fixAttachmentBlocks();
DROP PROCEDURE fixAttachmentBlocks;
