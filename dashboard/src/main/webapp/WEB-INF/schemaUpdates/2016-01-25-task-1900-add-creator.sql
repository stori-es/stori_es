ALTER TABLE systemEntity ADD creator INT(11) DEFAULT 0;
UPDATE systemEntity SET creator = owner;
