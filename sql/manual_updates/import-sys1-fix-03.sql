BEGIN;
-- story_field_values -> answerSet
  SET @answer_offset =(SELECT IF((SELECT count(*) FROM systemEntity) > 0,(SELECT MAX(id) + 1 FROM systemEntity),1));
-- we drive the answerSet off stories; this is how it's all tied in the SYS1
  INSERT INTO systemEntity (id, version, created, lastModified)
    SELECT (id + @answer_offset),1, created_at, updated_at
      FROM stories s
      JOIN importRecord i ON i.sourceId=s.id
      WHERE i.sourceTable='stories';
  INSERT INTO answerSet (id, respondent, questionnaire, locale)
    SELECT (s.id + @answer_offset),
      (SELECT targetId FROM importRecord WHERE sourceTable='people' AND sourceId=s.person_id),
       -- luckilly, there's still one questionnaire per collection for
       -- the imported collections; this will allow us to tie to the
       -- questionnaire
      (SELECT q.id FROM questionnaire q JOIN collection c ON q.collection=c.id JOIN collection_story cs ON cs.collection=c.id WHERE cs.story=s.id AND c.id <= 48706), -- that's the max imported collection; later collections add stories to other collections
      'en'
      FROM stories s
      JOIN importRecord i ON i.sourceId=s.id
      WHERE i.sourceTable='stories';

  SET @rank := 0;
  INSERT INTO answer (answerSet, idx, label, displayValue, reportValue)
    SELECT answerSet, rank, name, displayValue, reportValue FROM 
      (
       SELECT (s.id + @answer_offset) AS answerSet, @rank := @rank +1 AS rank , sf.name, sfv.content AS displayValue, sfv.content AS reportValue
         FROM story_field_values sfv
         JOIN story_fields sf ON sfv.story_field_id=sf.id
	 JOIN stories s ON sfv.story_id=s.id
	 JOIN importRecord i ON i.sourceId=s.id AND i.sourceTable='stories'
	 ORDER by sfv.id ASC
      ) AS tmp;
  INSERT INTO importRecord (sourceId, sourceTable, targetId, targetTable)
    SELECT s.id, 'stories', s.id + @answer_offset, 'answerSet'
      FROM stories s
      JOIN importRecord i ON i.sourceId=s.id
      WHERE i.sourceTable='stories';
COMMIT;
