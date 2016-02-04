ALTER TABLE questionOptions
ADD COLUMN displayValue VARCHAR(255);

UPDATE questionOptions q
  JOIN questionOptionsL10n qo
    ON q.questionnaire = qo.questionnaire
       AND q.questionIdx = qo.questionIdx
       AND q.idx = qo.idx
SET q.displayValue = qo.displayValue;

DROP TABLE IF EXISTS questionOptionsL10n;

ALTER TABLE question
ADD COLUMN text VARCHAR(255),
ADD COLUMN helpText VARCHAR(512);

UPDATE question q
  JOIN questionL10n qo
    ON q.questionnaire = qo.questionnaire
       AND q.idx = qo.idx
SET q.text = qo.text, q.helpText = qo.helpText;

DROP TABLE IF EXISTS questionL10n;
