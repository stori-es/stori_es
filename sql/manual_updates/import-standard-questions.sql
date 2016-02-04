ALTER TABLE questionL10n ENGINE=INNODB;
ALTER TABLE questionL10n ADD CONSTRAINT fk_questionL10n_question FOREIGN KEY (questionnaire, idx) REFERENCES question (questionnaire, idx);
ALTER TABLE questionOptionsL10n ENGINE=INNODB;
ALTER TABLE questionOptionsL10n ADD CONSTRAINT fk_questionOptionsL10n_questionOptions FOREIGN KEY (questionnaire, questionIdx, idx) REFERENCES questionOptions (questionnaire, questionIdx, idx);

DELIMITER //

CREATE PROCEDURE updateStandardQuestions()
BEGIN
  DECLARE questionnaireIdX INT;
  DECLARE done INT DEFAULT 0;

  DECLARE cur CURSOR FOR
    SELECT q.id
      FROM importRecord i 
      JOIN questionnaire q ON q.collection=i.targetId
      WHERE i.sourceTable='surveys';

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;

  OPEN cur;
  REPEAT
    FETCH cur INTO questionnaireIdX;
    IF NOT done THEN
      -- first, make space for the standard questions
      UPDATE question
        SET idx=idx + 10 WHERE questionnaire=questionnaireIdX;
      -- now insert the SYS1 standard questions
      INSERT INTO formElement
        (questionnaire, idx, formType, standardMeaning)
	VALUES
	(questionnaireIdX, 0, 'TEXT', 'FIRST NAME'),
	(questionnaireIdX, 1, 'TEXT', 'LAST NAME'),
	(questionnaireIdX, 2, 'TEXT', 'EMAIL'),
	(questionnaireIdX, 3, 'TEXT', 'STREET ADDRESS 1'),
	(questionnaireIdX, 4, 'TEXT', 'CITY'),
	(questionnaireIdX, 5, 'SELECT', 'STATE'),
	(questionnaireIdX, 6, 'TEXT', 'ZIP CODE'),
	(questionnaireIdX, 7, 'TEXT', 'PHONE'),
	(questionnaireIdX, 8, 'CHECKBOX', 'UPDATES OPT-IN'),
	(questionnaireIdX, 9, 'SELECT', 'PREFERRED EMAIL FORMAT');
      INSERT INTO question
        (questionnaire, idx, dataTYpe, label, required, minLength, maxLength, multiselect)	
      	VALUES
	(questionnaireIdX, 0, 'NAME', 'First Name', 1, 1, 255, 0),
	(questionnaireIdX, 1, 'NAME', 'Last Name', 1, 1, 255, 0),
	(questionnaireIdX, 2, 'EMAIL', 'Email', 1, 1, 255, 0),
	(questionnaireIdX, 3, NULL, 'Street Address', 1, 1, 255, 0),
	(questionnaireIdX, 4, NULL, 'City', 1, 1, 255, 0), 
	(questionnaireIdX, 5, NULL, 'State', 1, 1, 255, 0),
	(questionnaireIdX, 6, 'ZIP', 'Zip Code', 1, 1, 255, 0),
	(questionnaireIdX, 7, 'PHONE_NUMBER', 'Phone Number', 0, 1, 255, 0),
	(questionnaireIdX, 8, NULL, 'Updates Opt-in', 0, 1, 255, 0),
	(questionnaireIdX, 9, NULL, 'Email Format', 0, 1, 255, 0);
      INSERT INTO questionL10n
        (questionnaire, idx, locale, text, helpText)
	VALUES
	(questionnaireIdX, 0, 'en', 'First Name', NULL),
	(questionnaireIdX, 1, 'en', 'Last Name', NULL),
	(questionnaireIdX, 2, 'en', 'Email', NULL),
	(questionnaireIdX, 3, 'en', 'Street Addresss', NULL),
	(questionnaireIdX, 4, 'en', 'City', NULL),
	(questionnaireIdX, 5, 'en', 'State', NULL),
	(questionnaireIdX, 6, 'en', 'Zip Code', NULL),
	(questionnaireIdX, 7, 'en', 'Phone Number', NULL),
	(questionnaireIdX, 8, 'en', 'Updates Opt-In', NULL),
	(questionnaireIdX, 9, 'en', 'Email Format', NULL);
      INSERT INTO questionOptions
        (questionnaire, questionIdx, idx, reportValue)
	VALUES
	(questionnaireIdX, 5, 0, 'AL');
	-- (questionnaireIdX, 5, 1, 'AK'),
	-- (questionnaireIdX, 5, 2, 'AR'),
	-- (questionnaireIdX, 5, 3, 'AZ'),
	-- (questionnaireIdX, 5, 4, 'CA'),
	-- (questionnaireIdX, 5, 5, 'CO'),
	-- (questionnaireIdX, 5, 6, 'CT'),
	-- (questionnaireIdX, 5, 7, 'DC'),
	-- (questionnaireIdX, 5, 8, 'DE'),
	-- (questionnaireIdX, 5, 9, 'FL'),
	-- (questionnaireIdX, 5, 10, 'GA'),
	-- (questionnaireIdX, 5, 11, 'HI'),
	-- (questionnaireIdX, 5, 12, 'ID'),
	-- (questionnaireIdX, 5, 13, 'IL'),
	-- (questionnaireIdX, 5, 14, 'IN'),
	-- (questionnaireIdX, 5, 15, 'IA'),
	-- (questionnaireIdX, 5, 16, 'KS'),
	-- (questionnaireIdX, 5, 17, 'KY'),
	-- (questionnaireIdX, 5, 18, 'LA'),
	-- (questionnaireIdX, 5, 19, 'ME'),
	-- (questionnaireIdX, 5, 20, 'MD'),
	-- (questionnaireIdX, 5, 21, 'MA'),
	-- (questionnaireIdX, 5, 22, 'MI'),
	-- (questionnaireIdX, 5, 23, 'MN'),
	-- (questionnaireIdX, 5, 24, 'MS'),
	-- (questionnaireIdX, 5, 25, 'MO'),
	-- (questionnaireIdX, 5, 26, 'MT'),
	-- (questionnaireIdX, 5, 27, 'NE'),
	-- (questionnaireIdX, 5, 28, 'NV'),
	-- (questionnaireIdX, 5, 29, 'NH'),
	-- (questionnaireIdX, 5, 30, 'NJ'),
	-- (questionnaireIdX, 5, 31, 'NM'),
	-- (questionnaireIdX, 5, 32, 'NY'),
	-- (questionnaireIdX, 5, 33, 'NC'),
	-- (questionnaireIdX, 5, 34, 'ND'),
	-- (questionnaireIdX, 5, 35, 'OH'),
	-- (questionnaireIdX, 5, 36, 'OK'),
	-- (questionnaireIdX, 5, 37, 'OR'),
	-- (questionnaireIdX, 5, 38, 'PA'),
	-- (questionnaireIdX, 5, 39, 'RI'),
	-- (questionnaireIdX, 5, 40, 'SC'),
	-- (questionnaireIdX, 5, 41, 'SD'),
	-- (questionnaireIdX, 5, 42, 'TN'),
	-- (questionnaireIdX, 5, 43, 'TX'),
	-- (questionnaireIdX, 5, 44, 'UT'),
	-- (questionnaireIdX, 5, 45, 'VT'),
	-- (questionnaireIdX, 5, 46, 'VA'),
	-- (questionnaireIdX, 5, 47, 'WA'),
	-- (questionnaireIdX, 5, 48, 'WV'),
	-- (questionnaireIdX, 5, 49, 'WI'),
	-- (questionnaireIdX, 5, 50, 'WY'),
	-- (questionnaireIdX, 8, 0, 'Yes, I would like to receive periodic email updates. (Sí, me gustaría recibir boletines con información.)'),
	-- (questionnaireIdX, 9, 0, 'HTML'),
	-- (questionnaireIdX, 9, 1, 'PLAIN');
      -- INSERT INTO questionOptionsL10n
      --   (questionnarie, questionIdx, idx, locale, displayValue)
      -- 	VALUES
      -- 	-- state
      -- 	(questionnaireIdX, 5, 0, 'en', 'AL'),
      -- 	(questionnaireIdX, 5, 1, 'en', 'AK'),
      -- 	(questionnaireIdX, 5, 2, 'en', 'AR'),
      -- 	(questionnaireIdX, 5, 3, 'en', 'AZ'),
      -- 	(questionnaireIdX, 5, 4, 'en', 'CA'),
      -- 	(questionnaireIdX, 5, 5, 'en', 'CO'),
      -- 	(questionnaireIdX, 5, 6, 'en', 'CT'),
      -- 	(questionnaireIdX, 5, 7, 'en', 'DC'),
      -- 	(questionnaireIdX, 5, 8, 'en', 'DE'),
      -- 	(questionnaireIdX, 5, 9, 'en', 'FL'),
      -- 	(questionnaireIdX, 5, 10, 'en', 'GA'),
      -- 	(questionnaireIdX, 5, 11, 'en', 'HI'),
      -- 	(questionnaireIdX, 5, 12, 'en', 'ID'),
      -- 	(questionnaireIdX, 5, 13, 'en', 'IL'),
      -- 	(questionnaireIdX, 5, 14, 'en', 'IN'),
      -- 	(questionnaireIdX, 5, 15, 'en', 'IA'),
      -- 	(questionnaireIdX, 5, 16, 'en', 'KS'),
      -- 	(questionnaireIdX, 5, 17, 'en', 'KY'),
      -- 	(questionnaireIdX, 5, 18, 'en', 'LA'),
      -- 	(questionnaireIdX, 5, 19, 'en', 'ME'),
      -- 	(questionnaireIdX, 5, 20, 'en', 'MD'),
      -- 	(questionnaireIdX, 5, 21, 'en', 'MA'),
      -- 	(questionnaireIdX, 5, 22, 'en', 'MI'),
      -- 	(questionnaireIdX, 5, 23, 'en', 'MN'),
      -- 	(questionnaireIdX, 5, 24, 'en', 'MS'),
      -- 	(questionnaireIdX, 5, 25, 'en', 'MO'),
      -- 	(questionnaireIdX, 5, 26, 'en', 'MT'),
      -- 	(questionnaireIdX, 5, 27, 'en', 'NE'),
      -- 	(questionnaireIdX, 5, 28, 'en', 'NV'),
      -- 	(questionnaireIdX, 5, 29, 'en', 'NH'),
      -- 	(questionnaireIdX, 5, 30, 'en', 'NJ'),
      -- 	(questionnaireIdX, 5, 31, 'en', 'NM'),
      -- 	(questionnaireIdX, 5, 32, 'en', 'NY'),
      -- 	(questionnaireIdX, 5, 33, 'en', 'NC'),
      -- 	(questionnaireIdX, 5, 34, 'en', 'ND'),
      -- 	(questionnaireIdX, 5, 35, 'en', 'OH'),
      -- 	(questionnaireIdX, 5, 36, 'en', 'OK'),
      -- 	(questionnaireIdX, 5, 37, 'en', 'OR'),
      -- 	(questionnaireIdX, 5, 38, 'en', 'PA'),
      -- 	(questionnaireIdX, 5, 39, 'en', 'RI'),
      -- 	(questionnaireIdX, 5, 40, 'en', 'SC'),
      -- 	(questionnaireIdX, 5, 41, 'en', 'SD'),
      -- 	(questionnaireIdX, 5, 42, 'en', 'TN'),
      -- 	(questionnaireIdX, 5, 43, 'en', 'TX'),
      -- 	(questionnaireIdX, 5, 44, 'en', 'UT'),
      -- 	(questionnaireIdX, 5, 45, 'en', 'VT'),
      -- 	(questionnaireIdX, 5, 46, 'en', 'VA'),
      -- 	(questionnaireIdX, 5, 47, 'en', 'WA'),
      -- 	(questionnaireIdX, 5, 48, 'en', 'WV'),
      -- 	(questionnaireIdX, 5, 49, 'en', 'WI'),
      -- 	(questionnaireIdX, 5, 50, 'en', 'WY'),
      -- 	-- opt in
      -- 	(questionnaireIdX, 8, 0, 'en', 'Yes, I would like to receive free periodic consumer updates from Consumers Union Advocacy. (Sí, me gustaría recibir boletines con información del consumidor de Consumers Union.)'),
      -- 	-- email formats
      -- 	(questionnaireIdX, 9, 0, 'en', 'HTML'),
      -- 	(questionnaireIdX, 9, 1, 'en', 'Plain Text');
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL updateStandardQuestions();
DROP PROCEDURE updateStandardQuestions;

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