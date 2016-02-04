INSERT INTO formElement (questionnaire, idx, formType)
  SELECT
    c.id,
    0,
    'COLLECTION'
  FROM collection c
    LEFT OUTER JOIN questionnaire q ON c.id = q.id
  WHERE q.id IS NULL;

INSERT INTO contentL10n (questionnaire, idx, locale, content)
  SELECT
    c.id,
    0,
    'en',
    c.id
  FROM collection c
    LEFT OUTER JOIN questionnaire q ON c.id = q.id
  WHERE q.id IS NULL;
