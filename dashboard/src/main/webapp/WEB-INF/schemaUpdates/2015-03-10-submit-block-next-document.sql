ALTER TABLE submitBlock
ADD COLUMN nextDocument INT,
ADD CONSTRAINT fk_submitBlock_document FOREIGN KEY (nextDocument) REFERENCES document (id);

UPDATE submitBlock s
SET s.nextDocument = (SELECT confirmation
                      FROM questionnaire q
                      WHERE q.id = s.questionnaire);
