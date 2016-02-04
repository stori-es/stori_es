DROP TABLE IF EXISTS exportTask;
DROP TABLE IF EXISTS addStoriesTask;
DROP TABLE IF EXISTS task;
CREATE TABLE task (
  id        INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  profile   INT,
  type      VARCHAR(32),
  status    VARCHAR(32),
  count     INT DEFAULT 0,
  total     INT DEFAULT 0,
  created   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lastModified TIMESTAMP NULL DEFAULT NULL,
  CONSTRAINT fk_task_user FOREIGN KEY (profile) REFERENCES profile (id)
);

CREATE TABLE exportTask (
  id         INT,
  kind       VARCHAR(32),
  container  VARCHAR(32),
  objectId   INT,
  url        VARCHAR(255),
  expires    TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_exportTask_id FOREIGN KEY (id) REFERENCES task (id),
  CONSTRAINT fk_exportTask_collection FOREIGN KEY (objectId) REFERENCES collection (id)
);

CREATE TABLE addStoriesTask (
  id          INT,
  collections VARCHAR(512),
  searchToken VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT fk_addStoriesTask_id FOREIGN KEY (id) REFERENCES task (id)
);
