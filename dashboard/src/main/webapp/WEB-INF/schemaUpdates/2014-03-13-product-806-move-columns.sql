ALTER TABLE collection ADD COLUMN theme INT(11) NOT NULL;

UPDATE collection, questionnaire
SET collection.theme = questionnaire.theme
WHERE collection.id = questionnaire.id;

-- The first step is to get all the 'questionnaire.permalink' fields unique
UPDATE questionnaire q1
  JOIN questionnaire q2 ON q1.permalink = q2.permalink
SET q1.permalink = CONCAT(q1.permalink, '-', SUBSTR(MD5(RAND()), 27))
WHERE q1.id < q2.id;
-- Now we update the entity.permalink with the contextualized q.permalink field 
UPDATE entity e
  JOIN questionnaire q ON e.id = q.id
SET e.permalink = CONCAT('/questionnaires/', q.permalink);
-- Now we update the Collections, which are not Questionnaires.
UPDATE entity e
  JOIN collection c ON e.id = c.id
  LEFT OUTER JOIN questionnaire q ON c.id = q.id
SET e.permalink = concat('/collections/', e.permalink)
WHERE q.id IS NULL;

UPDATE entity, questionnaire
SET entity.title = questionnaire.title
WHERE entity.id = questionnaire.id;

ALTER TABLE questionnaire
DROP FOREIGN KEY fk_questionnaire_theme,
DROP COLUMN theme,
DROP COLUMN title,
DROP COLUMN permalink;

ALTER TABLE formElement
DROP FOREIGN KEY fk_formElement_questionnaire,
ADD CONSTRAINT fk_formElement_collection FOREIGN KEY (questionnaire) REFERENCES collection (id);
