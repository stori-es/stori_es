ALTER TABLE document ADD COLUMN locale VARCHAR(8) NOT NULL;

UPDATE document
SET locale = "en";

ALTER TABLE answerSet DROP COLUMN locale;
