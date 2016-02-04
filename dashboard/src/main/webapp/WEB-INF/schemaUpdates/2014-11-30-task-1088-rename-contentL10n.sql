DROP TABLE IF EXISTS content;

RENAME TABLE contentL10n TO content;

ALTER TABLE content
DROP PRIMARY KEY,
ADD PRIMARY KEY (questionnaire, idx),
DROP COLUMN locale;
