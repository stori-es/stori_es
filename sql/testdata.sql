INSERT INTO acl_class (id, class) VALUES
  (0, 'org.consumersunion.stories.common.client.model.SystemEntity');

-- root turn off foreign key checks for a moment; all entities are
-- 'owned' by root as a convenience to get along with Spring ACL, but
-- when we first insert the systemEntity, this triggers an
-- acl_object_identity entry which tries to reference the acl_sid (as
-- 'owner_sid') which isn't created till the profile insert
SET foreign_key_checks=0;
SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO';
INSERT INTO systemEntity (id, version,public) VALUES (0,1,0);
INSERT INTO entity (id, profile, permalink) VALUES (0, null, null);
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES
  (0, 'root', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', null, null, 0, 500);
INSERT INTO acl_sid VALUES(0, 1, 0);

-- SystemEntity 2
-- NOTE: A trigger creates an implifit root Profile with ID 3 to match the organization.
INSERT INTO systemEntity (id, version) VALUES (2,1);
INSERT INTO entity (id, profile, permalink) VALUES (2, null, 'http://consumersunion.org');
INSERT INTO organization (id, name, shortName, defaultTheme) VALUES (2, 'Consumer''s Union', 'CU', NULL);

INSERT INTO systemEntity (id, version,public,owner) VALUES (500,1,0,0);
INSERT INTO entity (id, profile, permalink) VALUES (500, null, null);
INSERT INTO api_keys(user, uuid) VALUES(0, '12345678901234567890');

-- Default Theme
INSERT INTO systemEntity (id, version, public, owner) VALUES (90,1,1,500);
INSERT INTO theme (id, name, themePage) VALUES
(90, 'Your health security','yhs.jsp');
UPDATE organization SET defaultTheme=90 WHERE id=2;

INSERT INTO systemEntity (id, version) VALUES (20002, 1);
INSERT INTO entity (id) VALUES (20002);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (20002, 1, 'EN', 'permissions', 2, 'DEFAULT_PERMISSIONS', 0);
INSERT INTO block (document, version, idx, blockType) VALUES (20002, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (20002, 1, 0, 'permissions', 'PLAIN');

-- SystemEntity 1
INSERT INTO systemEntity (id, version,public) VALUES (1,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (1, null, 'user1');
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier,defaultProfile) VALUES (1, 'testUser', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What was the name of your middle school?', 'Middle School', null, 1001);
INSERT INTO systemEntity (id, version, public,owner) VALUES (1001,1,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (1001, null, 'person1');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1001, 'Person', 'One', 1, 2);
SET foreign_key_checks=1;
INSERT INTO api_keys(user, uuid) VALUES(1, 'abcdef0123456789');

INSERT INTO systemEntity (id, version, public,owner) VALUES (10001,1,1,null);
INSERT INTO entity (id, profile, permalink) VALUES (10001, null, 'personToDeleteSuccess');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (10001, 'Person', 'To Delete', null, 2);

-- SystemEntity 4
INSERT INTO systemEntity (id, version, public, owner) VALUES (4,1,1,2);
INSERT INTO entity (id, profile, permalink) VALUES (4, null, 'test-collection');
INSERT INTO collection (id) VALUES (4);
INSERT INTO systemEntity (id, version) VALUES (804, 1);
INSERT INTO entity (id) VALUES (804);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (804, 1, 'EN', 'Easy Questionnaire', 4, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (804, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (804, 1, 1, 'test-collection', 'PLAIN');

-- SystemEntity 5
INSERT INTO systemEntity (id, version, public, owner) VALUES (5, 1, 1, 2);
INSERT INTO entity (id, profile, permalink) VALUES (5, null, 'test-collection1');
INSERT INTO collection (id) VALUES (5);
INSERT INTO systemEntity (id, version) VALUES (805, 1);
INSERT INTO entity (id) VALUES (805);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (805, 1, 'EN', 'Easy Questionnaire1', 5, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (805, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (805, 1, 1, 'test-collection1', 'PLAIN');

-- SystemEntity 1090
INSERT INTO systemEntity (id, version,public) VALUES (1090,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (1090, null, 'user1090');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1090, 'Person', '90', null, 2);

-- SystemEntity 33 (because 3 is taken, see above)
INSERT INTO systemEntity (id, version, public, owner) VALUES (33,1,1,2);
INSERT INTO entity (id, profile, permalink) VALUES (33, null, '/questionnaires/title-questionnaire-5');
INSERT INTO collection (id, theme) VALUES (33, 90);
INSERT INTO questionnaire (id) VALUES (33);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (4, 33);
-- Body doc.
INSERT INTO systemEntity (id, version) VALUES (8003, 1);
INSERT INTO entity (id) VALUES (8003);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8003, 1, 'EN', 'title questionnaire 5', 33, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8003, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8003, 1, 0, 'title questionnaire 5', 'PLAIN');

INSERT INTO systemEntity (id, version, public, owner) VALUES (3300, 1, 1, 2);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (3300, 1, 'en', 'The Survey', 33, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 0, 'TEXT_INPUT');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 0, 'EMAIL', 'Email', 1, NULL, NULL, 'Email', 'Enter your email address.');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 1, 'RADIO');

INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 1, null, 'Gender', 1, NULL, NULL, 'Gender', 'Gender');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 1, 0, 'male', 'male');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 1, 1, 'female', 'female');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 2, 'SELECT');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 2, null, 'favoriteMovie', 1, NULL, NULL, 'Favorite movie?', 'Cinema experiences and films also acceptable.');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 2, 0, 'lastVacation', 'northern hemisphere');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 2, 1, 'baz', 'southern hemisphere');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 3, 'CHECKBOX');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 3, null, 'starWars', 1, NULL, NULL, 'Which hand do you write with?', 'If you can''t remember, try picking up a pen.');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 3, 0, 'left', 'left');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES(3300, 1, 3, 1, 'right', 'right');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 4, 'TEXT_AREA');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 4, null, 'more', 1, NULL, NULL, 'Tell me more.', 'Please share anything else you''d like share.');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 5, 'RICH_TEXT_AREA');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (3300, 1, 5, null, 'Free Text', 1, NULL, NULL, 'Free text area', 'Please be free to share anything else you''d like share.');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 6, 'CONTENT');
INSERT INTO block_content (document, version, idx, content) VALUES (3300, 1, 6, 'Content');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 7, 'SUBHEADER');
INSERT INTO block_content (document, version, idx, content) VALUES (3300, 1, 7, 'Section Header');

INSERT INTO block (document, version, idx, blockType) VALUES (3300, 1, 8, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (3300, 1, 8, 'CENTER', 'MEDIUM', 'SUBMIT', null);

-- SystemEntity 52
INSERT INTO systemEntity (id, version,public) VALUES (52,1,0);
INSERT INTO entity (id, profile, permalink) VALUES (52, null, 'aLink');
INSERT INTO collection (id) VALUES (52);
INSERT INTO systemEntity (id, version) VALUES (8052, 1);
INSERT INTO entity (id) VALUES (8052);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8052, 1, 'EN', 'Sample collection', 52, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8052, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8052, 1, 1, 'aLink', 'PLAIN');

-- SystemEntity 958
INSERT INTO systemEntity (id, version, public, owner) VALUES (958, 1, 0, 2);
INSERT INTO entity (id, profile, permalink) VALUES (958, null, 'aLink2');
INSERT INTO collection (id) VALUES (958);
INSERT INTO systemEntity (id, version) VALUES (80958, 1);
INSERT INTO entity (id) VALUES (80958);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80958, 1, 'EN', 'Sample collection 2', 958, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80958, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80958, 1, 1, 'aLink2', 'PLAIN');

-- SystemEntity 959
INSERT INTO systemEntity (id, version, public, owner) VALUES (959, 1, 1, 2);
INSERT INTO entity (id, profile, permalink) VALUES (959, null, 'aLink3');
INSERT INTO collection (id) VALUES (959);
INSERT INTO systemEntity (id, version) VALUES (80959, 1);
INSERT INTO entity (id) VALUES (80959);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80959, 1, 'EN', 'Public collection', 959, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80959, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80959, 1, 1, 'aLink3', 'PLAIN');

-- SystemEntity 960
INSERT INTO systemEntity (id, version, public, owner) VALUES (960,1, FALSE, 2);
INSERT INTO entity (id, permalink) VALUES (960, '/questionnaires/share7');
INSERT INTO collection (id, theme) VALUES (960, 90);
INSERT INTO questionnaire (id) VALUES (960);
INSERT INTO systemEntity (id, version) VALUES (80960, 1);
INSERT INTO entity (id) VALUES (80960);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80960, 1, 'EN', 'title questionnaire 7', 960, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80960, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80960, 1, 1, '', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (90960, 1);
INSERT INTO entity (id) VALUES (90960);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (90960, 1, 'EN', 'title questionnaire 7 SURVEY', 960, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (90960, 1, 1, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (90960, 1, 1, 'CENTER', 'MEDIUM', 'SUBMIT', null);

-- SystemEntity 961
INSERT INTO systemEntity (id, version, public, owner) VALUES (961,1, TRUE, 2);
INSERT INTO entity (id, permalink) VALUES (961, '/questionnaires/share8');
INSERT INTO collection (id, theme, published) VALUES (961, 90, TRUE);
INSERT INTO questionnaire (id) VALUES (961);
INSERT INTO systemEntity (id, version) VALUES (80961, 1);
INSERT INTO entity (id) VALUES (80961);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80961, 1, 'EN', 'title questionnaire 8', 961, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80961, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80961, 1, 1, '', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (90961, 1);
INSERT INTO entity (id) VALUES (90961);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (90961, 1, 'EN', 'title questionnaire 8 SURVEY', 961, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (90961, 1, 1, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (90961, 1, 1, 'CENTER', 'MEDIUM', 'SUBMIT', null);


-- SystemEntity 962
INSERT INTO systemEntity (id, version, public, owner) VALUES (962,1, FALSE, 2);
INSERT INTO entity (id, permalink) VALUES (962, '/questionnaires/share9');
INSERT INTO collection (id, theme, published) VALUES (962, 90, TRUE);
INSERT INTO questionnaire (id) VALUES (962);
INSERT INTO systemEntity (id, version) VALUES (80962, 1);
INSERT INTO entity (id) VALUES (80962);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80962, 1, 'EN', 'title questionnaire 9', 962, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80962, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80962, 1, 1, '', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (90962, 1);
INSERT INTO entity (id) VALUES (90962);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (90962, 1, 'EN', 'title questionnaire 9 SURVEY', 962, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (90962, 1, 1, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (90962, 1, 1, 'CENTER', 'MEDIUM', 'SUBMIT', null);

-- SystemEntity 963
INSERT INTO systemEntity (id, version, public, owner) VALUES (963,1, TRUE, 2);
INSERT INTO entity (id, permalink) VALUES (963, '/questionnaires/share10');
INSERT INTO collection (id, theme, published) VALUES (963, 90, FALSE);
INSERT INTO questionnaire (id) VALUES (963);
INSERT INTO systemEntity (id, version) VALUES (80963, 1);
INSERT INTO entity (id) VALUES (80963);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (80963, 1, 'EN', 'title questionnaire 10', 963, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (80963, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (80963, 1, 1, '', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (90963, 1);
INSERT INTO entity (id) VALUES (90963);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (90963, 1, 'EN', 'title questionnaire 10 SURVEY', 963, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (90963, 1, 1, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (90963, 1, 1, 'CENTER', 'MEDIUM', 'SUBMIT', null);

-- SystemEntity 51
INSERT INTO systemEntity (id, version, public, owner) VALUES (51,1, TRUE, 2);
INSERT INTO entity (id, permalink) VALUES (51, '/questionnaires/share2');
INSERT INTO collection (id, theme) VALUES (51, 90);
INSERT INTO questionnaire (id) VALUES (51);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (5, 51);
INSERT INTO systemEntity (id, version) VALUES (8051, 1);
INSERT INTO entity (id) VALUES (8051);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8051, 1, 'EN', 'title questionnaire 6', 51, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8051, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8051, 1, 1, '/questionnaires/share2', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (98051, 1);
INSERT INTO entity (id) VALUES (98051);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (98051, 1, 'EN', 'title questionnaire 6 SURVEY', 51, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (98051, 1, 0, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (98051, 1, 0, 'CENTER', 'MEDIUM', 'SUBMIT', null);
INSERT INTO block (document, version, idx, blockType) VALUES (98051, 1, 2, 'TEXT_INPUT');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (98051, 1, 2, null, 'Gender1', 1, NULL, NULL, 'Email', 'Email');
INSERT INTO block (document, version, idx, blockType) VALUES (98051, 1, 3, 'STORY_ASK_PLAIN');
INSERT INTO block_question (document, version, idx, dataType, label, required, minLength, maxLength, text, helpText) VALUES (98051, 1, 3, null, 'Gender2', 1, NULL, NULL, 'Email', 'Email');

-- SystemEntity 53
INSERT INTO systemEntity (id, version,public) VALUES (53,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (53, null, 'person2');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES(53, 'test', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What is your favorite color?', 'blue', null, 1053);
INSERT INTO systemEntity (id, version, public) VALUES (1053,1,0);
INSERT INTO entity (id, profile, permalink) VALUES (1053, null, 'person53');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1053, 'Person', 'Two', 53, 2);
SET foreign_key_checks=1;
INSERT INTO api_keys(user, uuid) VALUES(53, 'abcdefabcdef');

-- SystemEntity 56
INSERT INTO systemEntity (id, version,public) VALUES (50,1,0);
INSERT INTO entity (id, profile, permalink) VALUES (50, null, 'person50');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES(50, 'test50', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What is your favorite color?', 'blue', null, 1053);
INSERT INTO systemEntity (id, version, public) VALUES (1050,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (1050, null, 'profile50');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1050, 'Person', '50', 50, 2);
SET foreign_key_checks=1;

-- SystemEntity 54
INSERT INTO systemEntity (id, version,public) VALUES (54,1,1);
INSERT INTO `document` (`id`,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`) VALUES
(54,54,'BODY',1001,'rings');

-- SystemEntity 55
INSERT INTO systemEntity (id, version,public) VALUES (55,1,0);
INSERT INTO `document` (`id`,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`) VALUES
(55,55,'BODY',1001,'code');

-- SystemEntity 56
INSERT INTO systemEntity (id, version,public) VALUES (56,1,1);
INSERT INTO `document` (`id`,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`) VALUES
(56,56,'BODY',1001,'amt');

-- SystemEntity 6 -- 19
INSERT INTO systemEntity (id, version,public,owner) values (6, 1, 1,1), (7, 1, 1,1), (8, 1, 0,1), (9, 1, 1,1), (10, 1, 0,1), (11, 1, 1,1), (12, 1, 0,53), (13, 1, 1,53), (14, 1, 0,53), (15, 1, 0,1), (16, 1, 0,1), (17, 1, 0,1), (18, 1, 0,1), (19, 1, 0,1);
INSERT INTO story (id, permalink, owner, defaultContent, published, firstPublished, byline) values
(6, 'permalink', 1001, 55, 1, '2000-01-09 00:00:00', 'User1'),
(7, 'permalink', 1001, 55, 1, '2000-01-08 00:00:00', 'User1'),
(8, 'permalink', 1001, 55, 1, '2000-01-12 00:00:00', 'User1'),
(9, 'permalink', 1001, 55, 1, '2000-01-12 00:00:00', 'User1'),
(10, 'permalink', 1001, 55, 1, '2000-01-12 00:00:00', 'User1'),
(11, 'permalink', 1001, 55, 1, '2000-01-12 00:00:00', 'User1'),
(12, 'permalink', 1053, 55, 1, '2000-01-12 00:00:00', 'User2'),
(13, 'permalink', 1053, 55, 1, '2000-01-12 00:00:00', 'User2'),
(14, 'permalink', 1053, 55, 1, '2000-01-12 00:00:00', 'User2'),
(15, 'permalink', 1001, 55, 1, '2000-09-03 00:00:00', 'User1'),
(16, 'permalink', 1001, 55, 1, '2000-09-04 00:00:00', 'User1'),
(17, 'permalink', 1001, 55, 1, '2000-08-02 00:00:00', 'User1'),
(18, 'permalink', 1001, 55, 1, '2000-01-06 00:00:00', 'User1'),
(19, 'permalink', 1001, 55, 1, '2000-05-01 00:00:00', 'User1');

-- SystemEntity 6 -- 19
INSERT INTO systemEntity (id, version,public) values (101, 1, 0), (102, 1, 0), (103, 1, 0), (104, 1, 0), (105, 1, 0), (106, 1, 1), (107, 1, 0), (108, 1, 0);
INSERT INTO document (`id`,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale,version) VALUES
       (101, 6, 'ANSWER_SET', 1001, '','en',1),
       (102, 7, 'ANSWER_SET', 1001, '','en',1),
       (103, 8, 'ANSWER_SET', 1001, '','en',1),
       (104, 9, 'ANSWER_SET', 1001, '','en',1),
       (105, 10, 'ANSWER_SET', 1001, '','en',1),
       (106, 11, 'ANSWER_SET', 1001, '','en',1),
       (107, 12, 'ANSWER_SET', 1001, '','en',1),
       (108, 13, 'ANSWER_SET', 1001, '','en',1);
INSERT INTO answerSet (id, questionnaire) values
       (101, 33), (102, 33), (103, 33), (104, 33), (105, 33), (106, 33), (107, 33), (108, 33);

INSERT INTO collection_story (collection, clearedForPublicInclusion, story) VALUES
(52, 1, 15),
(52, 1, 16),
(52, 0, 17),
(52, 0, 18);

INSERT INTO systemEntity (id, version,public) VALUES (57,1,1);
INSERT INTO document (`id`,`version`,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale, title) VALUES
	(57,1,15,'BODY',1001,'','en', 'doc57');
INSERT INTO block (document, version, idx, blockType) VALUES (57, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (57, 1, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed placerat ligula easy sit amet lectus tempus consequat. Cras turpis massa, placerat sit amet consequat in, auctor ac ipsum. Donec metus risus, sodales at mollis et, vehicula vitae dui. Ut vitae turpis metus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse consectetur nisl vitae metus volutpat vel placerat justo aliquet. Sed non erat ligula. Phasellus posuere leo neque, vel suscipit nisi. Duis eu odio mi, vitae tristique urna.

Donec eu elit ac turpis placerat congue. Suspendisse potenti. Nullam sit amet odio a est fermentum tempus eu sit amet sem. Suspendisse ornare feugiat lectus aliquet imperdiet. In commodo placerat odio, eget posuere odio rutrum mollis. Quisque volutpat suscipit augue, laoreet laoreet ipsum vulputate sit amet. Integer leo nisi, adipiscing quis bibendum ut, sollicitudin vel nunc. Integer dolor arcu, mattis nec tempor eget, fringilla non lacus. Etiam mattis libero a tortor pellentesque ac ultricies erat faucibus. Ut rutrum est et nisi varius sagittis. Cras et velit arcu. Nulla vel turpis in est lobortis vestibulum. Aenean metus lectus, porta ornare dictum blandit, bibendum quis lectus.', 'PLAIN');

INSERT INTO systemEntity (id, version,public) VALUES (58,1,1);
INSERT INTO document (`id`,version,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale, title) VALUES
	(58,1,16,'BODY',1001,'','en', 'doc58');
INSERT INTO block (document, version, idx, blockType) VALUES (58, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (58, 1, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed placerat ligula sit amet lectus tempus consequat. Cras turpis massa, placerat sit amet consequat in, auctor ac ipsum. Donec metus risus, sodales at mollis et, vehicula vitae dui. Ut vitae turpis metus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse consectetur nisl vitae metus volutpat vel placerat justo aliquet. Sed non erat ligula. Phasellus posuere leo neque, vel suscipit nisi. Duis eu odio mi, vitae tristique urna.

Donec eu elit ac turpis placerat congue. Suspendisse potenti. Nullam sit amet odio a est fermentum tempus eu sit amet sem. Suspendisse ornare feugiat lectus aliquet imperdiet. In commodo placerat odio, eget posuere odio rutrum mollis. Quisque volutpat suscipit augue, laoreet laoreet ipsum vulputate sit amet. Integer leo nisi, adipiscing quis bibendum ut, sollicitudin vel nunc. Integer dolor arcu, mattis nec tempor eget, fringilla non lacus. Etiam mattis libero a tortor pellentesque ac ultricies erat faucibus. Ut rutrum est et nisi varius sagittis. Cras et velit arcu. Nulla vel turpis in est lobortis vestibulum. Aenean metus lectus, porta ornare dictum blandit, bibendum quis lectus.', 'PLAIN');

INSERT INTO systemEntity (id, version,public) VALUES (59,1,1);
INSERT INTO document (`id`,version,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale, title) VALUES
	(59,1,17,'BODY',1001,'','en', 'doc59');
INSERT INTO block (document, version, idx, blockType) VALUES (59, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (59, 1, 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed placerat ligula easy sit amet lectus tempus consequat. Cras turpis massa, placerat sit amet consequat in, auctor ac ipsum. Donec metus risus, sodales at mollis et, vehicula vitae dui. Ut vitae turpis metus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Suspendisse consectetur nisl vitae metus volutpat vel placerat justo aliquet. Sed non erat ligula. Phasellus posuere leo neque, vel suscipit nisi. Duis eu odio mi, vitae tristique urna.

Donec eu elit ac turpis placerat congue. Suspendisse potenti. Nullam sit amet odio a est fermentum tempus eu sit amet sem. Suspendisse ornare feugiat lectus aliquet imperdiet. In commodo placerat odio, eget posuere odio rutrum mollis. Quisque volutpat suscipit augue, laoreet laoreet ipsum vulputate sit amet. Integer leo nisi, adipiscing quis bibendum ut, sollicitudin vel nunc. Integer dolor arcu, mattis nec tempor eget, fringilla non lacus. Etiam mattis libero a tortor pellentesque ac ultricies erat faucibus. Ut rutrum est et nisi varius sagittis. Cras et velit arcu. Nulla vel turpis in est lobortis vestibulum. Aenean metus lectus, porta ornare dictum blandit, bibendum quis lectus.', 'PLAIN');

-- SystemEntity 20, 21, 22, 23
INSERT INTO systemEntity (id, version,public) VALUES (20,1,1);
INSERT INTO systemEntity (id, version,public) VALUES (21,1,1);
INSERT INTO systemEntity (id, version,public) VALUES (22,1,1);
INSERT INTO systemEntity (id, version,public) VALUES (23,1,1);
INSERT INTO tag (systemEntity, value) VALUES (20, 'test'), (21, 'more test'), (22, 'health'), (23, 'health care');

-- SystemEntity 24, 25
INSERT INTO systemEntity (id, version,public) values (24, 1, 1), (25, 1, 1);
INSERT INTO entity (id, profile, permalink) VALUES (24, null, 'css1'),(25, null, 'css2');
INSERT INTO document (`id`,version,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale, title) VALUES
(24,1,15,'STYLE',1001,'css1','en','css24');

INSERT INTO document (`id`,version,`systemEntity`,`systemEntityRelation`,`primaryAuthor`,`permalink`,locale, title) VALUES
(25,1,16,'STYLE',1001,'css2','en','css25');

INSERT INTO block (document, version, idx, blockType) VALUES (24, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (24, 1, 1, 'body { color: blue; background-color: #F0F8FF; }', 'CSS');
INSERT INTO block (document, version, idx, blockType) VALUES (25, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (25, 1, 1, 'body { color: black; background-color: #F0F8FF; }', 'CSS');

--
-- ACL test data
--

-- SystemEntity 35
-- ACL test user#1 data and his Collections
INSERT INTO systemEntity (id, version) VALUES (35,1);
INSERT INTO entity (id) VALUES (35);
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES (35, 'aclUser1', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What was the name of your middle school?', 'Middle School', null, 1035);
INSERT INTO systemEntity (id, version, public,owner) VALUES (1035,1,1,35);
INSERT INTO entity (id, profile, permalink) VALUES (1035, null, 'person35');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1035, 'Person', 'ThirtyFive', 35, 2);
SET foreign_key_checks=1;

-- SystemEntity 38
-- ACL test user#2 data and his Collections
INSERT INTO systemEntity (id, version) VALUES (38,10);
INSERT INTO entity (id) VALUES (38);
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES (38, 'aclUser2', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What was the name of your middle school?', 'Middle School', null, 1038);
INSERT INTO systemEntity (id, version) VALUES (1038,10);
INSERT INTO entity (id) VALUES (1038);
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (1038, 'User to be updated', 'using JUnit',38,  2);
SET foreign_key_checks=1;

-- Address test data
INSERT INTO address (entity, idx, relation, address1, address2, city, state, country,  postalCode, latitude, longitude) VALUES
	(1, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(1, 1, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123),
	(35, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(35, 1, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123),
	(38, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(38, 1, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123),
	(53, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(53, 1, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123);

INSERT INTO entity (id) VALUES (10),(11),(12),(13),(14),(7),(8);

INSERT INTO address (entity, idx, relation, address1, address2, city, state, country,  postalCode, latitude, longitude) VALUES
	(10, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(11, 0, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123),
	(12, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(13, 0, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123),
	(14, 0, 'Work', 'Office Address', 'Address #2', 'Jacksonville', 'FL', 'US', '33010', 12.123, 12.123),
	(7, 0, 'Work', 'Office Address', 'Address #2', 'ORLANDO', 'FL', 'US', '33010', 12.123, 12.123),
	(8, 0, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123);


-- SystemEntity 1045
-- Person test data
INSERT INTO systemEntity (id, version) VALUES (1045,1);
INSERT INTO entity (id) VALUES (1045);
INSERT INTO profile (id, givenName, surname, organization) VALUES (1045, 'User to be deleted', 'using JUnit', 2);
INSERT INTO address (entity, idx, relation, address1, address2, city, state, country,  postalCode, latitude, longitude) VALUES
	(1045, 0, 'Home', 'Home Address', 'Address #2', 'Las Vegas', 'NV', 'US', '89044', 12.123, 12.123),
	(1045, 1, 'Work', 'Office Address', 'Address #2', 'Miami', 'FL', 'US', '33010', 12.123, 12.123);
INSERT INTO systemEntity (id, version) VALUES (45,1);
INSERT INTO entity (id) VALUES (45);
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, defaultProfile) VALUES(45, 'testUserToBeDeleted', 1,
'$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What was the name of your middle school?', 'Middle School', null, 1045);

-- Contact test data
INSERT INTO contact (entityId,idx,medium,type,value) VALUES
	(1001,0,'EMAIL','Home','testuser@home.com'),
	(1001,1,'EMAIL','Work','testuser@work.com'),
	(1001,2,'EMAIL','Other','testuser@other.com'),
	(1001,3,'PHONE','Home','(212) 630-0319'),
	(1001,4,'PHONE','Work','(212) 563-7440'),
	(1001,5,'PHONE','Other','(212) 663-7440'),
	(1001,6,'PHONE','Mobile','(212) 963-7440'),
	(1053,0,'EMAIL','Home','test@home.com'),
	(1053,1,'EMAIL','Work','test@work.com'),
	(1053,2,'PHONE','Home','(212) 630-0319'),
        (1053,3,'PHONE','Work','(212) 563-7440'),
	(1038,0,'EMAIL','Home','acluser2@home.com'),
	(1038,1,'EMAIL','Work','acluser2@work.com'),
	(1038,2,'PHONE','Home','(212) 630-0319'),
	(1038,3,'PHONE','Work','(212) 563-7440'),
	(1045,0,'EMAIL','Home','testUserToBeDeleted@home.com'),
	(1045,1,'EMAIL','Work','testUserToBeDeleted@work.com'),
	(1045,2,'PHONE','Home','(212) 630-0319'),
        (1045,3,'PHONE','Work','(212) 563-7440');

INSERT INTO systemEntity (id, version) VALUES (36,1);
INSERT INTO entity(id, profile, permalink) VALUES (36, NULL, 'acl-collection #36');
INSERT INTO collection (id) VALUES (36);
INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES (36,1035,100), (36,1001,300);
INSERT INTO systemEntity (id, version) VALUES (8036, 1);
INSERT INTO entity (id) VALUES (8036);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8036, 1, 'EN', 'acl-collection #36', 36, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8036, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8036, 1, 0, 'acl-collection #36', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (37,1);
INSERT INTO entity(id, profile, permalink) VALUES (37, NULL, 'acl-collection #37');
INSERT INTO collection (id) VALUES (37);
INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES (37,1035,200);
INSERT INTO systemEntity (id, version) VALUES (8037, 1);
INSERT INTO entity (id) VALUES (8037);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8037, 1, 'EN', 'acl-collection #37', 37, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8037, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8037, 1, 0, 'acl-collection #37', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (40,1);
INSERT INTO entity(id, profile, permalink) VALUES (40, NULL, 'acl-collection #40');
INSERT INTO collection (id) VALUES (40);
INSERT INTO systemEntity (id, version) VALUES (8040, 1);
INSERT INTO entity (id) VALUES (8040);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8040, 1, 'EN', 'acl-collection #40', 40, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8040, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8040, 1, 0, 'acl-collection #40', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (43,1);
INSERT INTO entity(id, profile, permalink) VALUES (43, NULL, 'acl-collection #39');
INSERT INTO collection (id) VALUES (43);
INSERT INTO systemEntity (id, version) VALUES (8043, 1);
INSERT INTO entity (id) VALUES (8043);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8043, 1, 'EN', 'acl-collection #43', 43, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8043, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8043, 1, 0, 'acl-collection #43', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (44,1);
INSERT INTO entity(id, profile, permalink) VALUES (44, NULL, 'acl-collection #44');
INSERT INTO collection (id) VALUES (44);
INSERT INTO systemEntity (id, version) VALUES (8044, 1);
INSERT INTO entity (id) VALUES (8044);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8044, 1, 'EN', 'acl-collection #44', 44, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8044, 1, 0, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8044, 1, 0, 'acl-collection #44', 'PLAIN');

INSERT INTO systemEntity (id, version) VALUES (46,1);
INSERT INTO entity (id) VALUES (46);
INSERT INTO collection (id, theme) VALUES (46, 90);
INSERT INTO questionnaire (id) VALUES (46);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (36, 46);
INSERT INTO systemEntity (id, version) VALUES (8046, 1);
INSERT INTO entity (id) VALUES (8046);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8046, 1, 'EN', 'title questionnaire 1', 46, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8046, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8046, 1, 1, '', 'PLAIN');
INSERT INTO systemEntity (id, version) VALUES (98046, 1);
INSERT INTO entity (id) VALUES (98046);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (98046, 1, 'EN', 'title questionnaire 1 SURVEY', 46, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (98046, 1, 0, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (98046, 1, 0, 'CENTER', 'MEDIUM', 'SUBMIT', null);

INSERT INTO systemEntity (id, version) VALUES (47,1);
INSERT INTO entity (id) VALUES (47);
INSERT INTO collection (id, theme) VALUES (47, 90);
INSERT INTO questionnaire (id) VALUES (47);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (37, 47);
INSERT INTO systemEntity (id, version) VALUES (8047, 1);
INSERT INTO entity (id) VALUES (8047);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8047, 1, 'EN', 'title questionnaire 2', 47, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8047, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8047, 1, 1, '', 'PLAIN');
INSERT INTO systemEntity (id, version) VALUES (98047, 1);
INSERT INTO entity (id) VALUES (98047);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (98047, 1, 'EN', 'title questionnaire 2 SURVEY', 47, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (98047, 1, 0, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (98047, 1, 0, 'CENTER', 'MEDIUM', 'SUBMIT', null);

INSERT INTO systemEntity (id, version) VALUES (49,1);
INSERT INTO entity (id) VALUES (49);
INSERT INTO collection (id, theme) VALUES (49, 90);
INSERT INTO questionnaire (id) VALUES (49);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (43, 49);
INSERT INTO systemEntity (id, version) VALUES (8049, 1);
INSERT INTO entity (id) VALUES (8049);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8049, 1, 'EN', 'title questionnaire 3', 49, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8049, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8049, 1, 1, '', 'PLAIN');
INSERT INTO systemEntity (id, version) VALUES (98049, 1);
INSERT INTO entity (id) VALUES (98049);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (98049, 1, 'EN', 'title questionnaire 3 SURVEY', 49, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (98049, 1, 0, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (98049, 1, 0, 'CENTER', 'MEDIUM', 'SUBMIT', null);

INSERT INTO systemEntity (id, version) VALUES (48,1);
INSERT INTO entity (id) VALUES (48);
INSERT INTO collection (id, theme) VALUES (48, 90);
INSERT INTO questionnaire (id) VALUES (48);
INSERT INTO collection_sources (targetCollection, sourceQuestionnaire) VALUES (44, 48);
INSERT INTO systemEntity (id, version) VALUES (8048, 1);
INSERT INTO entity (id) VALUES (8048);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (8048, 1, 'EN', 'title questionnaire 4', 48, 'BODY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (8048, 1, 1, 'CONTENT');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (8048, 1, 1, '', 'PLAIN');
INSERT INTO systemEntity (id, version) VALUES (98048, 1);
INSERT INTO entity (id) VALUES (98048);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (98048, 1, 'EN', 'title questionnaire 4 SURVEY', 48, 'SURVEY', 1001);
INSERT INTO block (document, version, idx, blockType) VALUES (98048, 1, 0, 'SUBMIT');
INSERT INTO block_submit (document, version, idx, position, size, prompt, nextDocument) VALUES (98048, 1, 0, 'CENTER', 'MEDIUM', 'SUBMIT', null);

-- Collection Story data
INSERT INTO collection_story (collection, story, clearedForPublicInclusion) VALUES
	(36,6,36),(37,7,37),(43,8,39),(44,9,40);
INSERT INTO collection_story (collection, story, clearedForPublicInclusion) VALUES
	(4,15,15),(4,16,16),(4,17,17),(4,18,18),(4,19,19);

-- SystemEntity 60
INSERT INTO systemEntity (id, version) VALUES (60,1);
INSERT INTO entity (id, profile, permalink) VALUES (60, null, 'http://domain.org');
INSERT INTO organization (id, name, defaultTheme) VALUES (60, 'Organization name', 90);

INSERT INTO systemEntity (id, version, public,owner) VALUES (10000,1,1,null);
INSERT INTO entity (id, profile, permalink) VALUES (10000, null, 'personToDeleteFail');
INSERT INTO profile (id, givenName, surname, user, organization) VALUES (10000, 'Person', 'To Delete', null, 60);

-- SystemEntity 28
INSERT INTO systemEntity (id, version) VALUES (28,1);
INSERT INTO entity (id, profile, permalink) VALUES (28, null, 'http://stori.es');
INSERT INTO organization (id, name, shortName) VALUES (28, 'stori.es', 'es');

-- SystemEntity 27
INSERT INTO systemEntity (id, version) VALUES (27,1);
INSERT INTO entity (id, profile, permalink) VALUES (27, null, 'http://Consumerist.org');
INSERT INTO organization (id, name, shortName) VALUES (27, 'Consumerist', 'CU');

-- Set up entities for authorization tests
-- Users  N  S  O  B  A  R  A'
--       61 62 63 64 65 66 67
INSERT INTO systemEntity (id, version,owner) VALUES (1061,1,61), (1062,1,62), (1063,1,63), (1064,1,64), (1065,1,65), (1066,1,66), (1067,1,67);
INSERT INTO entity (id) VALUES (1061), (1062),(1063),(1064),(1065),(1066),(1067);
SET foreign_key_checks=0;
INSERT INTO profile (id, givenName, surname, user, organization) VALUES
  (1061, 'Person', 'SixtyOne', 61, 2),
  (1062, 'Person', 'SixtyTwo', 62, 2),
  (1063, 'Person', 'SixtyThree', 63, 2),
  (1064, 'Person', 'SixtyFour', 64, 2),
  (1065, 'Person', 'SixtyFive', 65, 2),
  (1066, 'Person', 'SixtySix', 66, 2),
  (1067, 'Person', 'SixtySeven', 67, 2);
INSERT INTO systemEntity (id, version) VALUES (61, 0), (62, 0), (63, 0), (64, 0), (65, 0), (66, 0), (67, 0);
INSERT INTO entity (id, permalink) VALUES (61, 'Un'), (62, 'Us'), (63, 'Uo'), (64, 'Ub'), (65, 'Ua'), (66, 'Ur'), (67, 'Ua''');
-- the password is 'password'
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, defaultProfile) VALUES
  (61, 'Un', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1061),
  (62, 'Us', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1062),
  (63, 'Uo', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1063),
  (64, 'Ub', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1064),
  (65, 'Ua', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1065),
  (66, 'Ur', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1066),
  (67, 'Ua''', 1, '$2a$12$RvGh2XWDGIJUK7NzbRalqe61Cspm/jTTdhe./dwHP.OGn5D.9mcnu', 'What was the name of your middle school?', 'Middle School', 1067);
SET foreign_key_checks=1;


INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES (17,1061,300);

-- Set up organizations for authorization tests
-- Public Orgs  N  O  B  A  A'
--             68 69 70 71 72
INSERT INTO systemEntity (id, version) VALUES (68,1),(69,1),(70,1),(71,1),(72,1);
INSERT INTO entity (id, permalink) VALUES
  (68, 'Onu'), (69, 'Oou'), (70, 'Obu'), (71, 'Oau'), (72, 'Oau''');
INSERT INTO organization (id, name, defaultTheme) VALUES
  (68, 'Onu', 90), (69, 'Oou', 90), (70, 'Obu', 90), (71, 'Oau', 90), (72, 'Oau''', 90);

-- Set up organizations for authorization tests
-- Public Orgs  N  O  B  A  A'
--             73 74 75 76 77
INSERT INTO systemEntity (id, version, public) VALUES (73,1,TRUE),(74,1,TRUE),(75,1,TRUE),(76,1,TRUE),(77,1,TRUE);
INSERT INTO entity (id, permalink) VALUES
  (73, 'Onr'), (74, 'Oor'), (75, 'Obr'), (76, 'Oar'), (77, 'Oar''');
INSERT INTO organization (id, name, defaultTheme) VALUES
  (73, 'Onr', 90), (74, 'Oor', 90), (75, 'Obr', 90), (76, 'Oar', 90), (77, 'Oar''', 90);

-- Set up Users for authorization tests
-- Person CSR CSRW CSA CSR CSRW CSA
--         78   79  80  81   82  83
INSERT INTO systemEntity (id, version) VALUES (78, 0), (79, 0), (80, 0), (81, 0), (82, 0), (83, 0);
INSERT INTO entity (id, permalink) VALUES (78, 'Pcsr'), (79, 'Pcsrw'), (80, 'Pcsa'), (81, 'Pnsr'), (82, 'Pnsrw'), (83, 'Pcsa''');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, defaultProfile) VALUES
  (78, 'Pcsr', 1078), (79, 'Pcsrw', 1079), (80, 'Pcsa', 1080), (81, 'Pnsr', 1081), (82, 'Pnsrw', 1082), (83, 'Pcsa''', 1083);
INSERT INTO systemEntity (id, version,owner) VALUES (1078,1,78), (1079,1,79), (1080,1,80), (1081,1,81), (1082,1,82), (1083,1,83);
INSERT INTO entity (id) VALUES (1078),(1079),(1080),(1081),(1082),(1083);
INSERT INTO profile (id, user, organization) VALUES
  (1078, 78, 2),
  (1079, 79, 2),
  (1080, 80, 2),
  (1081, 81, 2),
  (1082, 82, 2),
  (1083, 83, 2);
SET foreign_key_checks=1;

-- Set up Persons for authorization tests
-- Person CSR CSRW CSA CSR CSRW CSA
--         84   85  86  87   88  89
INSERT INTO systemEntity (id, version) VALUES (84, 0), (85, 0), (86, 0), (87, 0), (88, 0), (89, 0);
INSERT INTO entity (id, permalink) VALUES (84, 'Pcor'), (85, 'Pcorw'), (86, 'Pcoa'), (87, 'Pnor'), (88, 'Pnorw'), (89, 'Pcoa''');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, defaultProfile) VALUES
  (84, 'Pcor', 1084), (85, 'Pcorw', 1085), (86, 'Pcoa', 1086), (87, 'Pnor', 1087), (88, 'Pnorw', 1088), (89, 'Pcoa''', 1089);
INSERT INTO systemEntity (id, version,owner) VALUES (1084,1,84), (1085,1,85), (1086,1,86), (1087,1,87), (1088,1,88), (1089,1,89);
INSERT INTO entity (id) VALUES (1084),(1085),(1086),(1087),(1088),(1089);
INSERT INTO profile (id, user, organization) VALUES
  (1084, 84, 2),
  (1085, 85, 2),
  (1086, 86, 2),
  (1087, 87, 2),
  (1088, 88, 2),
  (1089, 89, 2);
SET foreign_key_checks=1;

INSERT INTO systemEntity (id, version,public) VALUES (29,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (29, null, 'person29');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier,defaultProfile) VALUES (29, 'gregory', 1, '$2a$12$DS3YSYvzrMaPSb98RXDkk.AUo.8cGORf9gHQnHw78aFrtcGoxivqC', 'What was the name of your middle school?', 'Middle School', null,1029);
INSERT INTO systemEntity (id, version,owner) VALUES (1029,1,29);
INSERT INTO entity (id) VALUES (1029);
INSERT INTO profile (id, user, organization) VALUES (1029, 29, 2);
SET foreign_key_checks=1;

INSERT INTO systemEntity (id, version,public) VALUES (30,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (30, null, 'person30');
SET foreign_key_checks=0;
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier,defaultProfile) VALUES (30, 'dryan', 1, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', 'What was the name of your middle school?', 'Middle School', null,1030);
INSERT INTO systemEntity (id, version,owner) VALUES (1030,1,30);
INSERT INTO entity (id) VALUES (1030);
INSERT INTO profile (id, user, organization) VALUES (1030, 30, 2);
SET foreign_key_checks=1;

SET foreign_key_checks=0;
INSERT INTO systemEntity (id, version,public) VALUES (32,1,1);
INSERT INTO entity (id, profile, permalink) VALUES (32, null, 'user32');
INSERT INTO user (id, handle) VALUES (32, 'testCreatePerson');
SET foreign_key_checks=1;

INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES
	(1,1035,100),
	(2,1035,100),
	(33,1035,200),
	(1,1001,300);
-- user-53 reads story-8
INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES
	(8,1053,100);
INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES
	(2,1001,300),
	(33,1001,200),
	(4,1001,100),
	(4,1035,300),
	(6,1001,300),
	(1,1053,300),
	(5,1053,300),
	(2,1053,100),
	(33,1053,100),
	(9,1001,100),
	(10,1001,300),
	(11,1001,100),
	(12,1001,200),
	(13,1001,100),
	(14,1001,100),
	(15,1053,200),
	(16,1053,300),
	(17,1001,300),
	(17,1038,300),
	(44,1001,300),
	(38,1053,300);

INSERT INTO acl_entry (acl_object_identity, sid, mask) VALUES
	(10001,1001,300),
	(958,1001,300),
	(960,1001,300),
	(961,1001,300),
	(962,1001,300),
	(963,1001,300);

-- Setup test for API Documents-Blocks
INSERT INTO systemEntity (id, version, public, owner) VALUES (109, 1, 1, 2);
INSERT INTO entity (id, profile, permalink) VALUES (109, null, 'block-test-questionnaire');
INSERT INTO collection (id) VALUES (109);
INSERT INTO systemEntity (id, version) VALUES (110, 1);
INSERT INTO entity (id) VALUES (110);
INSERT INTO document (id, version, locale, title, systemEntity, systemEntityRelation, primaryAuthor) VALUES (110, 1, 'EN', 'Block Test Questionnaire', 109, 'BODY', 1001);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 1, 'ATTACHMENTS');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 1, 'question1', 0, 'Attachments');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 2, 'AUDIO');
INSERT INTO block_image (document, version, idx, url, caption, alttext, position, size) VALUES (110, 1, 2, 'http://foo.com/song.ogg', 'a cool song', 'this song is six words long', NULL, NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 3, 'SUBMIT');
-- Note, the 'nextDocument' is a loop; as good as any for our current use of the object but we should improve.
INSERT INTO block_submit (document, version, idx, nextDocument, size, position) VALUES (110, 1, 3, 8003, 'MEDIUM', 'CENTER');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 4, 'TEXT_INPUT');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 4, 'question2', 0, 'Single Line Text');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 5, 'TEXT_AREA');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 5, 'question3', 0, 'Multi Line Plain Text');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 6, 'RICH_TEXT_AREA');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 6, 'question4', 0, 'Multi Line Rich Text');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 7, 'CITY');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 7, 'question5', 0, 'City');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 8, 'FIRST_NAME');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 8, 'question6', 0, 'First Name');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 9, 'LAST_NAME');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 9, 'question7', 0, 'Last Name');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 10, 'COLLECTION');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (110, 1, 10, '4', NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 11, 'DATE');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 11, 'question8', 0, 'Date');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 12, 'STORY_ASK_RICH');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 12, 'question9', 0, 'Story Ask');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 13, 'STORY_TITLE');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 13, 'question10', 0, 'Story Title');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 14, 'STREET_ADDRESS_1');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 14, 'question11', 0, 'Street Address');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 15, 'DOCUMENT');
INSERT INTO block_link (document, version, idx, title, url) VALUES (110, 1, 15, 'a document', 'http://foo.bar/doc.pdf');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 16, 'PREFERRED_EMAIL_FORMAT');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 16, 'question12', 0, 'Preferred Email Format');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 16, 0, 'Yes', 'Yes');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 16, 1, 'No', 'No');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 17, 'STATE');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 17, 'question13', 0, 'State');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 0, 'AL', 'Alalbama');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 1, 'OK', 'Oklahoma');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 2, 'NV', 'Nevada');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 3, 'NY', 'New York');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 4, 'TX', 'Texas');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 5, 'WI', 'Wisconsin');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 17, 6, 'WY', 'Wyoming');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 18, 'SELECT');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 18, 'question14', 0, 'Select');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 18, 0, 'A', 'A');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 18, 1, 'B', 'B');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 18, 2, 'C', 'C');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 19, 'EMAIL');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 19, 'question15', 0, 'Email - Home');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 19, 'EMAIL', 'Home');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 20, 'EMAIL_WORK');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 20, 'question16', 0, 'Email - Work');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 20, 'EMAIL', 'Work');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 21, 'EMAIL_OTHER');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 21, 'question17', 0, 'Email - Other');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 21, 'EMAIL', 'Other');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 22, 'PHONE');
INSERT INTO block_question (document, version, idx, label, required, dataType, text) VALUES (110, 1, 22, 'question18', 0, 'PHONE_NUMBER', 'Phone - Home');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 22, 'PHONE', 'Home');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 23, 'PHONE_WORK');
INSERT INTO block_question (document, version, idx, label, required, dataType, text) VALUES (110, 1, 23, 'question19', 0, 'PHONE_NUMBER', 'Phone - Work');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 23, 'PHONE', 'Work');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 24, 'PHONE_MOBILE');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 24, 'question20', 0, 'Phone - Mobile');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 24, 'PHONE', 'Work');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 25, 'PHONE_OTHER');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 25, 'question21', 0, 'Phone - Other');
INSERT INTO question_contact (document, version, idx, type, opt) VALUES (110, 1, 25, 'PHONE', 'Work');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 26, 'IMAGE');
INSERT INTO block_image (document, version, idx, url, caption, alttext, position, size) VALUES (110, 1, 26, 'http://foo.bar/image.png', 'an image', 'image', 'LEFT', 'SMALL');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 27, 'TEXT_IMAGE');
INSERT INTO block_image (document, version, idx, url, caption, alttext, position, size) VALUES (110, 1, 27, 'http://bar.baz/image.png', 'another image', 'image2', 'RIGHT', 'LARGE');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (110, 1, 27, 'Some text', NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 28, 'CUSTOM_PERMISSIONS');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (110, 1, 28, 'All rights reserved.', NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 29, 'RATING_STARS');
-- TODO: shouldn't start/end/steps be numbers? See TASK-1719
INSERT INTO block_rating (document, version, idx, start, end, steps) VALUES (110, 1, 29, '1', '5', '1');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 30, 'RATING_NUMBERS');
INSERT INTO block_rating (document, version, idx, start, end, steps) VALUES (110, 1, 30, '1', '10', '0.5');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 31, 'STORY');
INSERT INTO block_content (document, version, idx, content, textType) VALUES (110, 1, 31, '6', NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 32, 'UPDATES_OPT_IN');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 32, 'question22', 0, 'Opt In');
INSERT INTO question_options (document, version, questionIdx, idx, reportValue, displayValue) VALUES (110, 1, 32, 0, 'Yes', 'Y');

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 33, 'VIDEO');
INSERT INTO block_image (document, version, idx, url, caption, alttext, position, size) VALUES (110, 1, 33, 'http://foo.com/video.mp4', 'a cool video', 'vid vid', NULL, NULL);

INSERT INTO block (document, version, idx, blockType) VALUES (110, 1, 34, 'ZIP_CODE');
INSERT INTO block_question (document, version, idx, label, required, text) VALUES (110, 1, 34, 'question23', 0, 'Zip Code');

INSERT INTO dbUpdate VALUES
('2012-03-15_task-systwo-237_update-constraints.sql'),
('2012-03-20_task-systwo-241_update-constraints.sql'),
('2012-03-26_task-systwo-282_add-questionL10n-table.sql'),
('2012-03-27_283-update_l10n_options.sql'),
('2012-04-12-update_passwords.sql'),
('2012-04-26-SYSTWO-357-update_answer.sql'),
('2012-05-03-SYSTWO-383-create_answerSet_table.sql'),
('2012-05-04-SYSTWO-393-refactor_DB.sql'),
('2012-05-07-require-question-label.sql'),
('2012-05-07-SYSTWO-392-update_content.sql'),
('2012-05-10_task-systwo-424_Story-publishedDate.sql'),
('2012-05-17_task-systwo-443-451_create_acl_tables.sql'),
('2012-05-17_task-systwo-456_SystemEntity-Public.sql'),
('2012-05-18-systwo-386-430-static-changes.sql'),
('2012-05-18_task-systwo-457_Entity-NewFields.sql'),
('2012-05-24_task-systwo-476.sql'),
('2012-05-25_task-systwo-485_multipl_questionnaires.sql'),
('2012-05-28_task-systwo-489_update-answer-anwerSet-tables.sql'),
('2012-05-29-task-sytwo-496-phone_table.sql'),
('2012-05-30-SYSTWO-509-update_questionnaire.sql'),
('2012-05-30-task-sytwo-488-questionnaire_data.sql'),
('2012-05-31-task-sytwo-513-story_byline.sql'),
('2012-05-31-task-sytwo-514-document_FKs.sql'),
('2012-06-01_systwo-521_update-address-pk.sql'),
('2012-06-06_systwo_539_create-contact-table.sql'),
('2012-06-18_task-systwo-650_add-columns-questionnaire.sql'),
('2012-06-19-systwo-672-multiple-addresses.sql'),
('2012-06-21-6-systwo-651-update-collection-fk.sql'),
('2012-06-26-task-696-grant-read-auth.sql'),
('2012-06-27-SYSTWO-573-fix_foreign_key_relations.sql'),
('2012-06-27-task-systwo-772-update-surveys.sql'),
('2012-06-28-SYSTWO-572-remove-userEmail.sql'),
('2012-06-29-SYSTWO-571-create_dbUpdate.sql'),
('2012-07-09-systwo-582-create-keys.sql'),
('2012-07-11-systwo-883-i15d_in_answerset.sql'),
('2012-07-13_task-systwo-924_Question-standardMeaning-field.sql'),
('2012-07-17_task-systwo-924_FormElement-standardMeaning-field.sql'),
('2012-07-18_task-systwo-931_FormElement-addUnique.sql'),
('2012-07-19-task-systwo-981.sql'),
('2012-07-19_task-systwo-952_answerSetPerson.sql'),
('2012-07-23_task-systwo-992.sql'),
('2012-07-31_task-systwo-905_multiselect.sql'),
('2012-08-06_task-systwo-1051.sql'),
('2012-08-06_task-systwo-993.sql'),
('2012-08-07_task-systwo-1017-std-meaning-fix.sql'),
('2012-08-08_systwo-685-update-address-relations.sql'),
('2012-08-10_systwo-1067-persist_multi_response_answers.sql'),
('2012-08-13_systwo-903-Add_organization_stories.sql'),
('2012-08-13_task-systwo-1070_add-unique-questionnaire-title.sql'),
('2012-08-14_task-systwo-410_user_handle_lowerCase.sql'),
('2012-08-16_task-systwo-444_add_shortName_column.sql'),
('2012-08-17_constrain-owner_sid.sql'),
('2012-08-17_systwo-1073-Organization_Permalink.sql'),
('2012-08-17_systwo-903-Add_organization_consumerist.sql'),
('2012-08-18_insert-root.sql'),
('2012-08-20_systwo-1061-OBO_rights_on_consumers_union.sql'),
('2012-08-21_systwo-1086-Add_defaultOrg_user_field.sql'),
('2012-08-22_systwo-1098-update_obo_auths3.sql'),
('2012-08-22_systwo-1098-update_obo_auths_extended3.sql'),
('2012-08-23_systwo-1098-update_obo_ryan_greg2.sql'),
('2012-08-24_systwo-1100-stories_triggers.sql'),
('2012-08-24_systwo-1082-Associate_themes_organizations.sql'),
('2012_08_27_systwo-1068-update_std_types.sql'),
('2012-08-27_systwo-1098-update_obo_users_risdon.sql'),
('2012-08-27_systwo-1098-update_obo_YHS_users_CU.sql'),
('2012-08-27_systwo-1098-update_obo_YHS_users_kathy.sql'),
('2012-08-29_systwo-1098.sql'),
('2012-08-30_systwo-1117-Refactor_systementity_insert.sql'),
('2012-09-12_systwo-1131-update_staff_and_admins.sql'),
('2012-09-17-import.sql'),
('2012-09-19-task-1122-move-collections.sql'),
('2012-09-20-task-systwo-1016.sql'),
('2012-10-01-update-user-admin-rights.sql'),
('2012-10-16-systwo-1257.sql'),
('2012-10-17-systwo-1257.sql'),
('2012-10-30-a-systmo-1415.sql'),
('2012-10-30-b-systmo-1415.sql'),
('2012-10-31-SYSTOW-1033-Thank_you_field.sql'),
('2012-10-31-systwo-1402-b.sql'),
('2012-11-01-story-grants.sql'),
('2012-11-01-storyteller-fix.sql'),
('2012-11-08-collection-links.sql'),
('2012-11-08-story-grants.sql'),
('2012-11-12-systwo-1415.sql'),
('2012-11-16-systwo-1206.sql'),
('2012-12-13-task-systhree-499.sql'),
('2012-12-27-systhree-624.sql'),
('2013-01-03-systhree-582.sql'),
('2013-02-06-delete-bogus-root-stories.sql'),
('2013-02-25-systhree-458.sql'),
('2013-02-26-systhree-278.sql'),
('2013-02-19-systhree-778.sql'),
('2013-02-26-systhree-277.sql'),
('2013-02-26-systhree-279.sql'),
('2013-03-04-bugfix-systhree-1059.sql'),
('2013-03-12-systhree-109-cr-themes.sql'),
('2013-03-14-remove-nawsa-other-organisations.sql'),
('2013-03-05-systhree-149-system-entity-owner.sql'),
('2013-03-13-sythree-109-correction.sql'),
('2013-03-19-systhree-771.sql'),
('2013-04-18-systhree-1080.sql'),
('2013-04-20-task-systhree-1056.sql'),
('2013-04-22-systhree-835.sql'),
('2013-04-26-systhree-568-system-entity-waiver_relation.sql'),
('2013-05-02-systhree-1148-fix-unpublished-questionnaires.sql'),
('2013-05-03-systhree-1156.sql'),
('2013-05-03-systhree-150-1120-geocoding.sql'),
('2013-05-10-systhree-260.sql'),
('2013-05-12-systhree-90-add-convio-sync-fields.sql'),
('2013-05-12-systhree-90-init-convio-sync-fields.sql'),
('2013-05-22-acl-object-and-entry-repair-for-persons.sql'),
('2013-05-22-address-index-fix.sql'),
('2013-05-26-index-systemEntity-owner.sql'),
('2013-05-26-systhree-1206-copy-cu-waiver.sql'),
('2013-06-05-systhree-1247-write-over-collection.sql'),
('2013-06-06-systhree-64-insert-convio-cu-fields.sql'),
('2013-06-19-grant-index-operation.sql'),
('2013-07-02-systhree-1206-set-fa-waiver.sql'),
('2013-07-04-sysfour-292-waiver-to-permissions.sql'),
('2013-07-10-sysfour-434-add-cons-id.sql'),
('2013-07-10-sysfour-434-add-index-to-contacts.sql'),
('2013-07-19-complete-org-admin-priv-set.sql'),
('2013-07-24-product-32-imagecontent.sql'),
('2013-07-25-product-29-documentcontent.sql'),
('2013-08-05-systhree-1206-update-fa-waiver.sql'),
('2013-08-08-add-convio-sync-status.sql'),
('2013-08-08-sysfour-227-contactContent.sql'),
('2013-08-12-sysfour-226-formElementConstraint.sql'),
('2013-08-13-task-37-faww-theme.sql'),
('2013-08-16-sysfour-226-updateContactContent.sql'),
('2013-08-20-syfour-579.sql'),
('2013-09-02-sysfour-450.sql'),
('2013-09-05-450-table-fix.sql'),
('2013-09-05-task-97.sql'),
('2013-09-06-sysfour-254.sql'),
('2013-09-12-sysfour-596-active-fa-policy-link.sql'),
('2013-11-07-task-307-collection-count.sql'),
('2013-11-11-task-322-square-contacts-and-questions.sql'),
('2013-12-05-task-332-display-attachment-title.sql'),
('2013-12-10-product-438-trim-titles.sql'),
('2014-01-20-task-520-label-disambiguation.sql'),
('2014-02-18_task-605_create-autotag-table.sql'),
('2014-02-19-task-70-questionnaire-publish.sql'),
('2014-02-24-product-366-questionnaire-publish-date.sql'),
('2014-02-25-task-397-change-contact-connection.sql'),
('2014-03-13-product-806-move-columns.sql'),
('2014-03-13-sysfour-507-note-update.sql'),
('2014-03-21-product-819-default-collection-block.sql'),
('2014-03-24-product-533-collection-publish.sql'),
('2014-03-25-product-533-restore-default-collection-block.sql'),
('2014-03-27-fix-collection-block-and-theme.sql'),
('2014-04-02-task-633-caption-longtext.sql'),
('2014-04-03-task-744.sql'),
('2014-04-15-task-673.sql'),
('2014-04-16-task-776-rename-person.sql'),
('2014-04-27-task-397-update-user-profile.sql'),
('2014-04-27-task-397-allow-user-to-be-deleted.sql'),
('2014-04-27-task-397-z-allow-user-to-be-deleted.sql'),
('2014-04-30-add-org-grants.sql'),
('2014-04-30-setup-roles-and-collection-grants.sql'),
('2014-05-18-task-785-create-fake-body.sql'),
('2014-05-26-add-org-admin-grants.sql'),
('2014-05-27-task-866-fix-profile-triggers.sql'),
('2014-05-29-task-730-fix-contact-type.sql'),
('2014-06-05-task-924-fix-grant-mask-swap.sql'),
('2014-06-11-task-935-publicize-stories.sql'),
('2014-07-02-task-946-remove-document-public.sql'),
('2014-07-03-task-102-task-984-fix-indexer-and-iterate-doc-model.sql'),
('2014-07-15-task-998-standard-type-update.sql'),
('2014-07-16-task-964-add_tjc_theme.sql'),
('2014-07-22-product-1092-defaultContent.sql'),
('2014-08-14-porduct-1147-move-locale-to-document.sql'),
('2014-08-27-task-1037-api-keys.sql'),
('2014-09-11-task-961-create-subscriptions-table.sql'),
('2014-09-18-task-1179-contact-add-status.sql'),
('2014-09-19-task-1205-create-verification-table.sql'),
('2014-09-25-task-1209-org-auth-workaround.sql'),
('2014-10-15-task-1273-org-auth-workaround.sql'),
('2014-10-22-task-1029-collection-questionnaire-body-document.sql'),
('2014-10-29-task-1233-document-update-time.sql'),
('2014-10-29-task-1299-fix-contact-question.sql'),
('2014-11-13-task-1338-set-answerset-locale.sql'),
('2014-11-13-task-1339-fix-story-links.sql'),
('2014-11-21-task-1357-clear-state.sql'),
('2014-11-30-task-1084-remove-questionL10n.sql'),
('2014-11-30-task-1087-move-documentText-fields-to-document.sql'),
('2014-11-30-task-1088-rename-contentL10n.sql'),
('2014-11-30-task-1361-repurpose-formElement.sql'),
('2014-12-16-product-1643-submit-block.sql'),
('2014-12-16-task-1354-roles.sql'),
('2015-03-18-rating-block.sql'),
('clean_data_base.sql'),
('import-sys1.sql');
