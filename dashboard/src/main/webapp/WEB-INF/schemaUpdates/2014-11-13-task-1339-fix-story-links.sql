INSERT INTO collection_story (collection, story, clearedForPublicInclusion)
  SELECT
    c.targetCollection,
    cs.story,
    cs.clearedForPublicInclusion
  FROM collection_story cs
    CROSS JOIN collection_sources c ON cs.collection = c.sourceQuestionnaire
                                       AND NOT EXISTS(SELECT 1
                                                      FROM collection_story cs2
                                                      WHERE
                                                        cs2.story = cs.story AND cs2.collection = c.targetCollection);
