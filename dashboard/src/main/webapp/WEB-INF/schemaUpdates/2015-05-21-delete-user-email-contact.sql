DELETE c FROM contact c
  JOIN user u ON c.entityId = u.id;

ALTER TABLE verification_nonce
DROP FOREIGN KEY fk_verification_profile,
ADD CONSTRAINT fk_verification_entity FOREIGN KEY (profile) REFERENCES systemEntity (id);
