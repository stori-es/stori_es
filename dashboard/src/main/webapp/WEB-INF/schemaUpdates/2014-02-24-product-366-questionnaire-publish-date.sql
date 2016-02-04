ALTER TABLE questionnaire ADD COLUMN publishedDate TIMESTAMP NULL DEFAULT NULL;
ALTER TABLE questionnaire ADD COLUMN previewKey VARCHAR(8) DEFAULT NULL;

UPDATE questionnaire
SET published = TRUE, publishedDate = NOW();
