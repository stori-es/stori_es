-- The original import had an error with the address import re. the
-- states; the where clause in the sub-select matched on 's.id=p.id',
-- it should have been 's.id=a.id' where 's' referes to the 'states'
-- table, 'p' the 'people' table and 'a' the 'addresses'. The original import used the 'addreses.id' as the 'address.idx' value, and we make use of that fact in the update.

UPDATE address a JOIN addresses ads ON ads.id=a.idx SET a.state=(SELECT abbreviation FROM states ss JOIN addresses ads2 ON ads2.state_id=ss.id WHERE ads2.id=ads.id);

-- now drop the SYS1.0 tables
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS asset_attachments;
DROP TABLE IF EXISTS assets;
DROP TABLE IF EXISTS campaign_topics;
DROP TABLE IF EXISTS campaigns;
DROP TABLE IF EXISTS corporate_bodies;
DROP TABLE IF EXISTS counties;
DROP TABLE IF EXISTS expressions;
DROP TABLE IF EXISTS emails;
DROP TABLE IF EXISTS email_preferences;
DROP TABLE IF EXISTS matching_operators;
DROP TABLE IF EXISTS operands;
DROP TABLE IF EXISTS ors;
DROP TABLE IF EXISTS people;
DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS policies;
DROP TABLE IF EXISTS policy_types;
DROP TABLE IF EXISTS schema_migrations;
DROP TABLE IF EXISTS searches;
DROP TABLE IF EXISTS sessions;
DROP TABLE IF EXISTS sorting_orders;
DROP TABLE IF EXISTS states;
DROP TABLE IF EXISTS stories;
DROP TABLE IF EXISTS story_ask_topics;
DROP TABLE IF EXISTS story_asks;
DROP TABLE IF EXISTS story_communications;
DROP TABLE IF EXISTS story_field_options;
DROP TABLE IF EXISTS story_field_types;
DROP TABLE IF EXISTS story_field_values;
DROP TABLE IF EXISTS story_fields;
DROP TABLE IF EXISTS story_notes;
DROP TABLE IF EXISTS story_versions;
DROP TABLE IF EXISTS survey_policies;
DROP TABLE IF EXISTS survey_providers;
DROP TABLE IF EXISTS surveys;
DROP TABLE IF EXISTS topics;
DROP TABLE IF EXISTS users;

SET foreign_key_checks = 1;
