UPDATE document d
SET d.summary = NULL
WHERE d.summary LIKE '<p>EXAMPLE TEXT CONTENT BLOCK</p>%';
