CREATE TABLE api_keys (
  user     INT,
  uuid     VARCHAR(32),
  uuid_bin BINARY(16),
  PRIMARY KEY (user, uuid),
  CONSTRAINT fk_api_keys_user FOREIGN KEY (user) REFERENCES user (id)
);

CREATE UNIQUE INDEX api_hex_index ON api_keys (uuid_bin);

CREATE TRIGGER api_keys_insert_uuid_bin BEFORE INSERT ON api_keys
FOR EACH ROW SET NEW.uuid_bin = UNHEX(REPLACE(NEW.uuid, '-', ''));

CREATE TRIGGER api_keys_update_uuid_bin BEFORE UPDATE ON api_keys
FOR EACH ROW SET NEW.uuid_bin = UNHEX(REPLACE(NEW.uuid, '-', ''));
