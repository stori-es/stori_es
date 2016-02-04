UPDATE block b
  JOIN document d ON d.id = b.document AND d.version = b.version
  JOIN story s ON s.id = d.systemEntity
SET b.documentType = 'TEXT_IMAGE'
WHERE b.documentType = 'CONTENT' AND s.id IS NOT NULL;
