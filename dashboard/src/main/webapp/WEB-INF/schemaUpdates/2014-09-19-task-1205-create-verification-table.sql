CREATE TABLE verification_nonce (
  profile INT,
  email   VARCHAR(255),
  nonce   VARCHAR(32),
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (profile, email),
  CONSTRAINT fk_verification_profile FOREIGN KEY (profile) REFERENCES profile (id)
);
