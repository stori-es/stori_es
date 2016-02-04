ALTER TABLE collection ADD COLUMN published TINYINT(1) NOT NULL DEFAULT FALSE;
ALTER TABLE collection ADD COLUMN publishedDate TIMESTAMP NULL DEFAULT NULL;
ALTER TABLE collection ADD COLUMN previewKey VARCHAR(8) DEFAULT NULL;

UPDATE collection
SET published = TRUE, publishedDate = NOW();

UPDATE collection AS c, questionnaire AS q
SET c.published = q.published, c.previewKey = q.previewKey, c.publishedDate = q.publishedDate
WHERE c.id = q.id;

ALTER TABLE questionnaire DROP COLUMN published, DROP COLUMN publishedDate, DROP COLUMN previewKey;

UPDATE collection
SET theme = 236166
WHERE theme = 0;
