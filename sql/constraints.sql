ALTER TABLE `question` ADD CONSTRAINT `fk_question_systementity` FOREIGN KEY `fk_question_systementity` (`idx`)
    REFERENCES `systementity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION; 
   
ALTER TABLE `apitier` ADD CONSTRAINT `fk_apitier_systementity` FOREIGN KEY `fk_apitier_systementity` (`id`)
    REFERENCES `systementity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

ALTER TABLE `complaint` ADD CONSTRAINT `fk_complaint_systementity` FOREIGN KEY `fk_complaint_systementity` (`id`)
    REFERENCES `systementity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

ALTER TABLE `document` ADD CONSTRAINT `fk_document_systementity` FOREIGN KEY `fk_document_systementity` (`id`)
    REFERENCES `systementity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION; 

ALTER TABLE `questionnaire` DROP FOREIGN KEY `fk_questionarie_systemEntity`;
ALTER TABLE `questionnaire` ADD CONSTRAINT `fk_questionnaire_entity` FOREIGN KEY `fk_questionnaire_entity` (`id`)
    REFERENCES `entity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;


