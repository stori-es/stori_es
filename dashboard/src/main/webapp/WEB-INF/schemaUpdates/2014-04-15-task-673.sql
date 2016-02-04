ALTER TABLE collection_story
DROP PRIMARY KEY,
DROP INDEX collection_story_unique_collection_story,
ADD PRIMARY KEY (collection, story),
DROP COLUMN idx;

INSERT INTO collection_story (collection, story, clearedForPublicInclusion)
  SELECT
    collection,
    story,
    1
  FROM (
         SELECT
           a.questionnaire AS collection,
           d.systemEntity  AS story,
           cs.collection      csc,
           cs.story           css
         FROM systemEntity se JOIN answerSet a ON a.id = se.id
           JOIN document d ON d.id = a.id
           LEFT JOIN collection_story cs ON cs.collection = a.questionnaire AND cs.story = d.systemEntity
         WHERE cs.collection IS NULL AND cs.story IS NULL) A;
