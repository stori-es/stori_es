UPDATE collection
SET publishedDate = NOW(), previewKey = NULL
WHERE published = 1 AND publishedDate IS NULL;
