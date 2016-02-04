UPDATE systemEntity se JOIN document d ON d.id = se.id
SET se.public = d.public
WHERE d.public != se.public;
ALTER TABLE document DROP COLUMN public;
