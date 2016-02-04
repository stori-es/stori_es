DROP TRIGGER IF EXISTS acl_sid_profile_user_delete;
DROP TRIGGER IF EXISTS delete_acl;

DELIMITER //
CREATE TRIGGER `delete_acl` BEFORE DELETE ON systemEntity FOR EACH ROW
  BEGIN
    DELETE FROM acl_entry
    WHERE sid = OLD.id;
    DELETE FROM acl_entry
    WHERE acl_object_identity = OLD.id;
    DELETE FROM acl_object_identity
    WHERE id = OLD.id;
    DELETE FROM acl_sid
    WHERE id = OLD.id;
  END / /
    DELIMITER;
