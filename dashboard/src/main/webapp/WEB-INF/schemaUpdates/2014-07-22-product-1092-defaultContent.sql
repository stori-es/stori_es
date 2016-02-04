ALTER TABLE story DROP FOREIGN KEY fk_story_document1;
ALTER TABLE story CHANGE defaultRepresentation defaultContent INT;
ALTER TABLE story ADD CONSTRAINT fk_story_document1 FOREIGN KEY (defaultContent) REFERENCES document (id);

UPDATE story s
  JOIN document d ON d.systemEntity = s.id
SET s.defaultContent = d.id
WHERE d.systemEntityRelation = 'BODY';
