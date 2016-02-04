-- in the original import the story mapping was off by one
UPDATE importRecord SET targetId=targetId - 1 WHERE sourceTable='stories';

ALTER TABLE importRecord ADD COLUMN targetTable VARCHAR(64), DROP KEY importRecord_uniq_source, ADD CONSTRAINT importRecord_unique UNIQUE (sourceId, sourceTable, targetTable);
ALTER TABLE importRecord ENGINE=INNODB;
ALTER TABLE importRecord ADD CONSTRAINT importRecord_fk_targetId FOREIGN KEY (targetId) REFERENCES systemEntity (id);

UPDATE importRecord SET targetTable='person' WHERE sourceTable='people';
UPDATE importRecord SET targetTable='user' WHERE sourceTable='users';
UPDATE importRecord SET targetTable='collection' WHERE sourceTable='surveys';
UPDATE importRecord SET targetTable='story' WHERE sourceTable='stories';
UPDATE importRecord SET targetTable='document' WHERE sourceTable='story_notes';
UPDATE importRecord SET targetTable='document' WHERE sourceTable='story_communications';
UPDATE importRecord SET targetTable='organization' WHERE sourceTable='corporate_bodies';