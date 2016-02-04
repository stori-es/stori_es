CREATE TABLE subscription (
  profile INT,
  target  INT,
  type    VARCHAR(32),
  active  TINYINT(1) DEFAULT 1,
  PRIMARY KEY (profile, target, type),
  CONSTRAINT fk_subscriptions_profile FOREIGN KEY (profile) REFERENCES profile (id),
  CONSTRAINT fk_subscriptions_target FOREIGN KEY (target) REFERENCES entity (id)
);
