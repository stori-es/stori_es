DROP TRIGGER IF EXISTS create_acl_sid_organization;
-- this is the old trigger
DROP TRIGGER IF EXISTS create_acl_sid_person;
DROP TRIGGER IF EXISTS create_acl_sid_user;
DROP TRIGGER IF EXISTS create_acl_entity;
DROP TRIGGER IF EXISTS entity_update;
DROP TRIGGER IF EXISTS delete_acl;
DROP TRIGGER IF EXISTS updateUserHandleInsertCalls;
DROP TRIGGER IF EXISTS updateUserHandleUpdateCalls;

/*creates a acl_sid entry when an org is created*/
CREATE TRIGGER `create_acl_sid_organization` AFTER INSERT ON organization FOR EACH ROW   INSERT INTO acl_sid (id, principal, sid) VALUES (NEW.id, TRUE, NEW.id) ;

/*creates an acl_sid entry when a person is created*/
CREATE TRIGGER `create_acl_sid_user` AFTER INSERT ON user FOR EACH ROW   INSERT INTO acl_sid (id, principal, sid) VALUES (NEW.id, TRUE, NEW.id) ;

/*updates the last modified time whenever a systemEntity is updated*/
CREATE TRIGGER entity_update BEFORE UPDATE ON `systemEntity`   FOR EACH ROW SET NEW.lastModified = NOW() ;

/*creates an acl_object_identity record whenever a systemEntity record is inserted*/
DELIMITER //
CREATE TRIGGER `create_acl_entity` AFTER INSERT ON systemEntity FOR EACH ROW
  BEGIN
    INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
      VALUES (NEW.id, 0, NEW.id, NULL, 0, FALSE);
  END//
  
/*removed acl_object idientity recoreds and acl_sid records when a systemEntity is deleted*/
CREATE TRIGGER `delete_acl` BEFORE DELETE ON systemEntity FOR EACH ROW
  BEGIN
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