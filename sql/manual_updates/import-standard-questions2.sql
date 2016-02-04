-- turn off the erroneous autoincrement
ALTER TABLE question CHANGE COLUMN questionnaire questionnaire INT(11) NOT NULL;
-- drop all the FK relations between the tables to allow us to push up the question indexes
ALTER TABLE questionOptions DROP FOREIGN KEY fk_questionOptions_question;
ALTER TABLE question DROP FOREIGN KEY fk_question_formElement;
ALTER TABLE formElement DROP FOREIGN KEY fk_formElement_questionnaire;
ALTER TABLE contentL10n DROP FOREIGN KEY fk_contentL10n_formElement;
ALTER TABLE formElement DROP PRIMARY KEY;
ALTER TABLE question DROP PRIMARY KEY;
ALTER TABLE questionL10n DROP PRIMARY KEY;
ALTER TABLE questionOptions DROP PRIMARY KEY;
ALTER TABLE questionOptionsL10n DROP PRIMARY KEY;

UPDATE formElement a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.idx=a.idx + 10
  WHERE i.sourceTable='surveys';
UPDATE contentL10n a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.idx=a.idx + 10
  WHERE i.sourceTable='surveys';
UPDATE question a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.idx=a.idx + 10
  WHERE i.sourceTable='surveys';
UPDATE questionL10n a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.idx=a.idx + 10
  WHERE i.sourceTable='surveys';
UPDATE questionOptions a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.questionIdx=a.questionIdx + 10
  WHERE i.sourceTable='surveys';
UPDATE questionOptionsL10n a
  JOIN questionnaire qa ON qa.id=a.questionnaire
  JOIN importRecord i ON qa.collection=i.targetId
  SET a.questionIdx=a.questionIdx + 10
  WHERE i.sourceTable='surveys';


ALTER TABLE question ADD PRIMARY KEY (questionnaire, idx);
ALTER TABLE formElement ADD PRIMARY KEY (questionnaire, idx);
-- re-add the dropped keys
ALTER TABLE question ADD CONSTRAINT fk_question_formElement FOREIGN KEY (questionnaire, idx) REFERENCES formElement (questionnaire, idx);
ALTER TABLE questionOptions ADD CONSTRAINT fk_questionOptions_question FOREIGN KEY (questionnaire, questionIdx) REFERENCES question (questionnaire, idx);
ALTER TABLE contentL10n ADD CONSTRAINT fk_contentL10n_formElement FOREIGN KEY (questionnaire, idx) REFERENCES formElement (questionnaire, idx);
ALTER TABLE formElemen ADD CONSTRAINT fk_formElement_questionnaire FOREIGN KEY (questionnaire) REFERENCES questionnaire (id);
ALTER TABLE questionL10n ADD PRIMARY KEY (questionnaire, idx);
ALTER TABLE questionOptions ADD PRIMARY KEY (questionnaire, questionIdx, idx);
ALTER TABLE questionOptionsL10n ADD PRIMARY KEY (questionnaire, questionIdx, idx);

ALTER TABLE questionL10n ENGINE=INNODB;
ALTER TABLE questionL10n ADD CONSTRAINT fk_questionL10n_question FOREIGN KEY (questionnaire, idx) REFERENCES question (questionnaire, idx);
ALTER TABLE questionOptionsL10n ENGINE=INNODB;
ALTER TABLE questionOptionsL10n ADD CONSTRAINT fk_questionOptionsL10n_questionOptions FOREIGN KEY (questionnaire, questionIdx, idx) REFERENCES questionOptions (questionnaire, questionIdx, idx);



-- First Name
-- Last Name
-- Email
-- Street Address
-- City
-- State
-- Zip Code
-- Phone (not required)
-- Updates Opt-In (not required)
-- Email Format (not required)