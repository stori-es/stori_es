ALTER TABLE documentText ADD COLUMN title VARCHAR(255);

-- It is 'story.title' field which was previously updated and
-- generally preferred to the 'document.title'.
UPDATE documentText dt
  JOIN document d ON d.id = dt.documentId
  JOIN story s ON s.id = d.systemEntity
SET dt.title = s.title
-- The 'ANSWER_SET' Document titles are universally '', so we don't
-- worry about them.
WHERE d.systemEntityRelation = 'BODY';

ALTER TABLE story DROP COLUMN title;
ALTER TABLE document DROP COLUMN title;
