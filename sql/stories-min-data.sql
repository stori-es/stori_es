-- initialize user root / password
INSERT INTO acl_class (id, class) VALUES 
  (0, 'org.consumersunion.stories.common.client.model.SystemEntity');
SET foreign_key_checks=0;
SET @disable_triggers = 1;
-- What's going on here? Well, root must have ID 0, but when you try
-- to insert 0, MySQL takes that as equivalent to NULL (!) and uses
-- the auto-increment value. So we're forced to just use the
-- auto-increment and then update the ID to 0 after the insert
INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
UPDATE systemEntity SET id=0 WHERE id=LAST_INSERT_ID();
SET @disable_triggers = NULL;
INSERT INTO entity (id, profile, permalink, title)
  VALUES (0, NULL, NULL, 'root');
INSERT INTO person (id, surname, givenName)
  VALUES (0, 'root', 'root');
INSERT INTO user (id, handle, active, localPassword, resetQuestion, resetAnswer, apiTier, handleLowerCase, defaultOrg)
  VALUES (0, 'root', TRUE, '$2a$12$TbGkyGr5IVMsXnlCWk5R1.Vc/UPE6eL.bttHgtdK98ZPUJe/JTrf6', NULL, NULL, NULL, 'root', 0);
SET foreign_key_checks=1;
-- INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
--  VALUES(LAST_INSERT_ID(), 0, LAST_INSERT_ID(), NULL, NULL, FALSE);

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Default Stori.es', 'sys.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Buzz Stori.es', 'buzz.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Defend Your Dollars', 'dyd.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Hear Us Now', 'hun.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Not in My Food', 'nimf.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Our Green Energy Future', 'ogef.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Buzz Stori.es', 'buzz.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'The Consumerist', 'consumerist.jsp');

INSERT INTO systemEntity (created, lastModified, version, public)
  VALUES (NOW(), NULL, 0, FALSE);
INSERT INTO theme (id, name, themePage)
  VALUES (LAST_INSERT_ID(), 'Your Health Security', 'yhs.jsp');


