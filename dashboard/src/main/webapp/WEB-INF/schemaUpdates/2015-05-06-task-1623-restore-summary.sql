UPDATE document d
  JOIN systemEntity se ON d.id = se.id AND se.version = d.version
  JOIN block b ON b.document = d.id AND d.version = b.version
  JOIN block_content bc ON b.document = bc.document AND b.idx = bc.idx AND b.version = bc.version
SET d.summary = SUBSTRING(bc.content, 1, 500)
WHERE b.document IN (
  SELECT *
  FROM (
         SELECT b.document
         FROM block b
           JOIN document d ON d.id = b.document AND b.version = d.version
           JOIN collection c ON c.id = d.systemEntity
         WHERE d.systemEntityRelation = 'BODY'
         GROUP BY b.document) T
) AND b.documentType = 'CONTENT' AND bc.content IS NOT NULL AND bc.content <> '';
