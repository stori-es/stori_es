UPDATE story s
  JOIN document d
    ON d.systemEntity = s.id AND d.systemEntityRelation IN ('BODY', 'ANSWER_SET') AND d.primaryAuthor <> s.owner
  JOIN profile p ON s.owner = p.id AND (p.user <> 0 OR p.user IS NULL)
SET d.primaryAuthor = s.owner;

UPDATE story s
  JOIN document d
    ON d.systemEntity = s.id AND d.systemEntityRelation IN ('BODY', 'ANSWER_SET') AND d.primaryAuthor <> s.owner
  JOIN systemEntity se ON s.id = se.id
  JOIN profile p ON d.primaryAuthor = p.id
SET s.owner = d.primaryAuthor, se.owner = d.primaryAuthor;
