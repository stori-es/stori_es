-- Two old triggers.
DROP TRIGGER IF EXISTS acl_sid_user;
DROP TRIGGER IF EXISTS acl_sid_organization;
DROP TRIGGER IF EXISTS acl_sid_person_user_create;
DROP TRIGGER IF EXISTS acl_sid_person_user_udpate;
DROP TRIGGER IF EXISTS acl_sid_person_user_delete;

DROP TRIGGER IF EXISTS acl_sid_profile_user_create;
DROP TRIGGER IF EXISTS acl_sid_profile_user_udpate;
DROP TRIGGER IF EXISTS acl_sid_profile_user_delete;
DROP TRIGGER IF EXISTS create_acl_entity;
DROP TRIGGER IF EXISTS api_keys_insert_uuid_bin;
DROP TRIGGER IF EXISTS api_keys_update_uuid_bin;
DROP TRIGGER IF EXISTS root_profile;
DROP TRIGGER IF EXISTS entity_update;
DROP TRIGGER IF EXISTS delete_acl;
DROP TRIGGER IF EXISTS updateUserHandleInsertCalls;
DROP TRIGGER IF EXISTS updateUserHandleUpdateCalls;

-- The reset script may run this before the schema is updated, so we
-- gate the next three triggers. It's necessary to wrap everything in
-- a procedure to use 'IF' for control flow.
DELIMITER //

/* Creates ACL entry when a profile associated to a user is inserted. */
CREATE TRIGGER acl_sid_profile_user_create AFTER INSERT ON profile
FOR EACH ROW
BEGIN
  IF NEW.user IS NOT NULL THEN
    SET @existing=(SELECT COUNT(*) FROM acl_sid WHERE id=NEW.id);
    IF @existing=0 THEN
      INSERT INTO acl_sid (id, principal, sid) VALUES (new.id, 1, new.id);
    END IF;
  END IF;
END;//

/* Creates ACL entry when a profile associated to a user is updated. */
CREATE TRIGGER acl_sid_profile_user_udpate AFTER UPDATE ON profile
FOR EACH ROW
BEGIN
  IF NEW.user IS NOT NULL THEN
    SET @existing=(SELECT COUNT(*) FROM acl_sid WHERE id=NEW.id);
    IF @existing=0 THEN
      INSERT INTO acl_sid (id, principal, sid) VALUES (new.id, 1, new.id);
    END IF;
  END IF;
END;//
DELIMITER ;

/*updates the last modified time whenever a systemEntity is updated*/
CREATE TRIGGER entity_update BEFORE UPDATE ON `systemEntity`   FOR EACH ROW SET NEW.lastModified = NOW() ;

/*creates an acl_object_identity record whenever a systemEntity record is inserted*/
CREATE TRIGGER `create_acl_entity` AFTER INSERT ON systemEntity FOR EACH ROW
  INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
    VALUES (NEW.id, 0, NEW.id, NULL, 0, FALSE);

CREATE TRIGGER api_keys_insert_uuid_bin BEFORE INSERT ON api_keys
FOR EACH ROW SET NEW.uuid_bin=UNHEX(REPLACE(NEW.uuid, '-', ''));

CREATE TRIGGER api_keys_update_uuid_bin BEFORE UPDATE ON api_keys
FOR EACH ROW SET NEW.uuid_bin=UNHEX(REPLACE(NEW.uuid, '-', ''));

DELIMITER //
CREATE TRIGGER `root_profile` AFTER INSERT ON organization FOR EACH ROW
BEGIN
  INSERT INTO systemEntity (version, public,owner) VALUES (1, 0,0);
  INSERT INTO entity (id) VALUES (LAST_INSERT_ID());
  INSERT INTO profile (id, surname, givenName, convioSyncStatus, user, organization)
    VALUES (LAST_INSERT_ID(), 'root', 'root', NULL, 0, NEW.id);
  INSERT INTO acl_sid (id, principal, sid)
    VALUES (NEW.id, TRUE, NEW.id);
END//

/*removed acl_object idientity recoreds and acl_sid records when a systemEntity is deleted*/
CREATE TRIGGER `delete_acl` BEFORE DELETE ON systemEntity FOR EACH ROW
  BEGIN
    DELETE FROM acl_entry WHERE sid=OLD.id;
    DELETE FROM acl_entry WHERE acl_object_identity=OLD.id;
    DELETE FROM acl_object_identity WHERE id=OLD.id;
    DELETE FROM acl_sid WHERE id=OLD.id;
  END//
DELIMITER ;

DELIMITER |

/*update the user's handle in lowercase before each insert*/
CREATE TRIGGER updateUserHandleInsertCalls BEFORE Insert ON user
FOR EACH ROW BEGIN
  SET NEW.handleLowerCase = LOWER(NEW.handle);
END;

|

DELIMITER ;

DELIMITER |

/*update the user's handle in lowercase before each update*/
CREATE TRIGGER updateUserHandleUpdateCalls BEFORE UPDATE ON user FOR EACH ROW BEGIN
 IF NOT (NEW.handle <=> OLD.handle) THEN
  SET NEW.handleLowerCase = LOWER(NEW.handle);
 END IF;
END;

|

DELIMITER ;
