ALTER TABLE document
ADD COLUMN version INT NOT NULL,
ADD COLUMN title VARCHAR(255),
DROP PRIMARY KEY,
ADD PRIMARY KEY (id, version);

INSERT INTO document (id, primaryAuthor, permalink, systemEntity, systemEntityRelation, locale, title, version)
  SELECT
    id,
    primaryAuthor,
    permalink,
    systemEntity,
    systemEntityRelation,
    locale,
    dt.title,
    dt.version
  FROM document
    JOIN documentText dt ON document.id = dt.documentId
ON DUPLICATE KEY UPDATE title = dt.title;

ALTER TABLE documentText
DROP COLUMN title;
