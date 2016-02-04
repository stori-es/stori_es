CREATE TABLE reset_password
(
  handle  VARCHAR(64)                         NOT NULL,
  nonce   VARCHAR(32)                         NOT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (handle),
  UNIQUE KEY fk_reset_password_handle (handle),
  CONSTRAINT fk_reset_password_handle FOREIGN KEY (handle) REFERENCES user (handle)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO contact (entityId, medium, type, value, idx, status)
  SELECT *
  FROM
    (SELECT
       u.id,
       medium,
       type,
       value,
       0,
       status
     FROM user u
       JOIN profile p ON u.defaultProfile = p.id
       JOIN
       (SELECT
          entityId,
          medium,
          type,
          value,
          status
        FROM contact c
        WHERE medium = 'EMAIL' AND status LIKE '%VERIFIED'
        ORDER BY FIELD(status, 'VERIFIED', 'UNVERIFIED'), FIELD(type, 'Home', 'Mobile', 'Other')) de
         ON de.entityId = u.defaultProfile) T
  GROUP BY id;
