DROP TRIGGER IF EXISTS document_entity_modified_date_insert;
DROP TRIGGER IF EXISTS document_entity_modified_date_update;
DROP TRIGGER IF EXISTS document_text_entity_modified_date;
DROP TRIGGER IF EXISTS document_entity_modified_date;

DELIMITER //
CREATE TRIGGER document_entity_modified_date_insert AFTER INSERT ON document
FOR EACH ROW
  BEGIN
    SET @time = (SELECT lastModified
                 FROM systemEntity
                 WHERE id = NEW.id);
    UPDATE systemEntity
    SET lastModified = @time
    WHERE id = NEW.systemEntity;
  END;
//

CREATE TRIGGER document_entity_modified_date_update AFTER UPDATE ON document
FOR EACH ROW
  BEGIN
    SET @time = (SELECT lastModified
                 FROM systemEntity
                 WHERE id = NEW.id);
    UPDATE systemEntity
    SET lastModified = @time
    WHERE id = NEW.systemEntity;
  END;
//
DELIMITER ;
