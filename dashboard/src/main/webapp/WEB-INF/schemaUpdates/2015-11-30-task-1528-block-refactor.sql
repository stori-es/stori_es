-- Need to allow some more room for the new names.
ALTER TABLE block MODIFY documentType VARCHAR(32) NOT NULL;
-- Handle the two with 'variations', rating and story ask.
UPDATE block SET documentType='RATING_NUMBERS' where documentType='NUMBERS' and standardMeaning='RATING';
UPDATE block SET documentType='RATING_STARS' where documentType='STARS' and standardMeaning='RATING';
UPDATE block SET documentType='STORY_ASK_RICH' where documentType='STORY_ASK' and standardMeaning='RICH_TEXT_AREA';
UPDATE block SET documentType='STORY_ASK_PLAIN' where documentType='STORY_ASK' and standardMeaning='TEXT_AREA';
-- All the others collapse into 'documentType'.
UPDATE block set documentType=standardMeaning WHERE standardMeaning IS NOT NULL AND standardMeaning NOT IN ('RATING', 'STORY_ASK');
-- Now we update the table schema.
ALTER TABLE block DROP standardMeaning;
ALTER TABLE block CHANGE documentType blockType VARCHAR(32) NOT NULL;