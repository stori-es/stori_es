SET @second_import=(SELECT id FROM entity WHERE title='Overtreatment' AND id != 235606);
UPDATE collection_story SET collection=235606 WHERE collection=@second_import;
UPDATE importRecord SET targetId=235606 WHERE targetId=@second_import;
DELETE FROM collection WHERE id=@second_import;
DELETE FROM entity WHERE id=@second_import;
DELETE FROM acl_entry WHERE acl_object_identity=@second_import;
DELETE FROM acl_object_identity WHERE id=@second_import;
DELETE FROM systemEntity WHERE id=@second_import;