-- query to find collections without any questionnaire
-- SELECT c.id, e.title FROM collection c JOIN entity e ON e.id=c.id LEFT OUTER JOIN questionnaire q ON q.collection=c.id WHERE q.id IS NULL;

-- survey : a way to use the same story_ask (questionnaire) through multiple instances; there is no exact analogue
-- story_asks -> question (story) + questionnaire
-- story_fields -> question (non-story)
-- story_field_types (table) -> formType (column)

-- the collection title (from the entity table) matches to surveys.name

SET @questionnaire_offset = (SELECT MAX(id) + 1 FROM systemEntity);

DELIMITER //
CREATE PROCEDURE importQuestionnaire()
BEGIN
  DECLARE surveyIdX, storyAskIdX INT;
  DECLARE surveyCreatedX, surveyUpdatedX DATETIME;
  DECLARE surveyNameX VARCHAR(255);

  DECLARE done INT DEFAULT 0;
  DECLARE surveyCur CURSOR FOR
    SELECT s.id, s.created_at, s.updated_at, s.name, s.story_ask_id FROM 
      (SELECT e.title FROM collection c JOIN entity e ON e.id=c.id LEFT OUTER JOIN questionnaire q ON q.collection=c.id WHERE q.id IS NULL) AS tmp
      JOIN surveys s on tmp.title=s.name;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;  

  OPEN surveyCur;
  REPEAT
    FETCH surveyCur INTO surveyIdX, surveyCreatedX, surveyUpdatedX, surveyNameX, storyAskIdX;
    IF NOT done THEN
      -- set up the questionnaire
      INSERT INTO systemEntity (id, version, created, lastModified, `public`)
        VALUES (surveyIdX + @questionnaire_offset, 0, surveyCreatedX, surveyUpdatedX, TRUE);
      SET @collectionId = (SELECT c2.id FROM collection c2 JOIN entity e2 ON c2.id=e2.id WHERE e2.title=surveyNameX);
      INSERT INTO questionnaire (id, collection, title, themePage, cssDoc, permalink)
        VALUES (surveyIdX + @questionnaire_offset, @collectionId, surveyNameX, 'buzz.jsp', NULL, MD5(surveyNameX));

      -- what are we pulling in?
      SELECT e.title AS collectionTitle FROM entity e JOIN collection c ON c.id=e.id WHERE c.id=@collection_id;
      SELECT title AS questionnaireTitle FROM questionnaire WHERE id=(surveyIdX + @questionnaire_offset);

      -- pull in the story ask (and title first)
      -- how many questions are we pulling in?
      SELECT COUNT(*) FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sf.story_ask_id=storyAskIdX;
      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning)
        SELECT surveyIdX + @questionnaire_offset, 0, 'TEXT', 'STORY TITLE' FROM story_asks sa WHERE id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required)
        SELECT surveyIdX + @questionnaire_offset, 0, NULL, 'Title', TRUE FROM story_asks sa WHERE id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, 0, "en", 'Title', NULL FROM story_asks sa WHERE id=storyAskIdX;
      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning)
        SELECT surveyIdX + @questionnaire_offset, 1, 'RICH_TEXT_AREA', 'STORY ASK RICH' FROM story_asks sa WHERE id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required)
        SELECT surveyIdX + @questionnaire_offset, 1, NULL, 'Tell us your story', TRUE FROM story_asks sa WHERE id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, 1, "en", 'Tell us your story', NULL FROM story_asks sa WHERE id=storyAskIdX;
      -- now to pull in the (non-story-ask) questions; we do each formType individually; story_fields are the non-story-ask questions, but they are related through the story_ask, which is in turn tied to the survey
      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning) -- we use sys1 story_fields.id for the idx; not contiguous, but that's okay
        SELECT surveyIdX + @questionnaire_offset, sf.id, 'TEXT', NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Text Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required)
        SELECT surveyIdX + @questionnaire_offset, sf.id, NULL, sf.name, FALSE FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Text Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, sf.id, "en", sf.name, NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Text Field' AND sf.story_ask_id=storyAskIdX;

      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning)
        SELECT surveyIdX + @questionnaire_offset, sf.id, 'RADIO', NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required)
        SELECT surveyIdX + @questionnaire_offset, sf.id, NULL, sf.name, FALSE FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, sf.id, "en", sf.name, NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX;
      -- with the options, we need separate the direct value options from the entity references
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Boolean' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;

      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning)
        SELECT surveyIdX + @questionnaire_offset, sf.id, 'SELECT', NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required, multiselect)
        SELECT surveyIdX + @questionnaire_offset, sf.id, NULL, sf.name, FALSE, FALSE FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, sf.id, "en", sf.name, NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;

      INSERT INTO formElement (questionnaire, idx, formType, standardMeaning)
        SELECT surveyIdX + @questionnaire_offset, sf.id, 'SELECT', NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO question (questionnaire, idx, dataType, label, required, multiselect)
        SELECT surveyIdX + @questionnaire_offset, sf.id, NULL, sf.name, FALSE, TRUE FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionL10n (questionnaire, idx, locale, text, helpText)
        SELECT surveyIdX + @questionnaire_offset, sf.id, "en", sf.name, NULL FROM story_fields sf JOIN story_field_types sft ON sf.story_field_type_id=sft.id WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX;
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", sfo.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id 
	WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptions (questionnaire, questionIdx, idx, reportValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;
      INSERT INTO questionOptionsL10n (questionnaire, questionIdx, idx, locale, displayValue)
        SELECT surveyIdX + @questionnaire_offset, sf.id, sfo.id, "en", cb.name FROM story_fields sf JOIN story_field_options sfo ON sfo.story_field_id=sf.id JOIN story_field_types sft ON sft.id=sf.story_field_type_id
	  JOIN corporate_bodies cb ON cb.id=sfo.entity_type 
	WHERE sft.name='Multi-Select Field' AND sf.story_ask_id=storyAskIdX AND sfo.entity_id IS NULL;

    END IF;
  UNTIL done END REPEAT;
  CLOSE surveyCur;
END //
DELIMITER ;

CALL importQuestionnaire();
DROP PROCEDURE importQuestionnaire;
