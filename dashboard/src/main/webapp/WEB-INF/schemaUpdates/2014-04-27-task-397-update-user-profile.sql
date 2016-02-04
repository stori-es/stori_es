DELETE u.* FROM user u LEFT JOIN profile p ON p.user = u.id
WHERE p.id IS NULL;
ALTER TABLE user ADD COLUMN defaultProfile INT(11);
UPDATE user u JOIN profile p ON p.user = u.id AND u.defaultOrg = p.organization
SET u.defaultProfile = p.id;
ALTER TABLE user DROP FOREIGN KEY fk_user_organization;
ALTER TABLE user DROP COLUMN defaultOrg;
ALTER TABLE user MODIFY defaultProfile INT(11) NOT NULL;
UPDATE user u JOIN profile p ON p.user = u.id
SET u.defaultProfile = p.id
WHERE u.defaultProfile = 0;
ALTER TABLE user ADD CONSTRAINT fk_user_profile FOREIGN KEY (defaultProfile) REFERENCES profile (id);
