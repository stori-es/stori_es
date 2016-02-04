DROP TABLE IF EXISTS rating;

CREATE TABLE ratingBlock (
  questionnaire INT NOT NULL,
  idx           INT NOT NULL,
  start         VARCHAR(25),
  end           VARCHAR(25),
  steps         VARCHAR(16),
  PRIMARY KEY (questionnaire, idx),
  FOREIGN KEY (questionnaire, idx) REFERENCES block (questionnaire, idx)
);
