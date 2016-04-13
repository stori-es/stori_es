UPDATE block b
  JOIN block_question bq ON b.document = bq.document AND b.idx = bq.idx AND b.version = bq.version
SET b.blockType = 'STORY_ASK_PLAIN'
WHERE blockType = 'TEXT_AREA' AND bq.label LIKE '%tell your story%';
