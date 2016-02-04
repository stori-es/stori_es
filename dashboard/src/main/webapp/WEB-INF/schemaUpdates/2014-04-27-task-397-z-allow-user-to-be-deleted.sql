ALTER TABLE user DROP FOREIGN KEY fk_user_profile;
ALTER TABLE user ADD CONSTRAINT fk_user_profile FOREIGN KEY (defaultProfile) REFERENCES profile (id)
  ON DELETE CASCADE;

ALTER TABLE profile DROP FOREIGN KEY fk_person_user;
ALTER TABLE profile ADD CONSTRAINT fk_profile_user FOREIGN KEY (user) REFERENCES user (id)
  ON DELETE CASCADE;

ALTER TABLE profile DROP FOREIGN KEY fk_person_org;
ALTER TABLE profile ADD CONSTRAINT fk_profile_org FOREIGN KEY (organization) REFERENCES organization (id)
  ON DELETE CASCADE;
