UPDATE block_content bc
SET textType = 'HTML'
WHERE
  (SELECT standardMeaning
   FROM block b
   WHERE b.idx = bc.idx AND b.document = bc.document AND b.version = bc.version) = 'CUSTOM_PERMISSIONS'
  OR bc.document IN (SELECT d.id
                     FROM document d
                     WHERE systemEntityRelation = 'DEFAULT_PERMISSIONS');

UPDATE block_content bco JOIN block b ON b.document = bco.document AND b.idx = bco.idx AND b.version = bco.version
SET textType = 'PLAIN', b.documentType = 'CONTENT'
WHERE (bco.document, bco.idx, bco.version)
      IN (SELECT *
          FROM (
                 SELECT DISTINCT
                   bc.document,
                   bc.idx,
                   bc.version
                 FROM block_content bc
                   JOIN block b ON b.document = bc.document AND b.idx = bc.idx AND
                                   b.version = bc.version
                   JOIN document d ON d.id = b.document AND d.version = b.version
                 WHERE d.systemEntityRelation <> 'ATTACHMENT'
                       AND d.systemEntityRelation <> 'NOTE'
                       AND bc.content LIKE "%\n%"
                       AND b.documentType <> 'COLLECTION'
                       AND b.documentType <> 'STORY') t);

UPDATE block_content bco
SET textType = 'HTML'
WHERE (bco.document, bco.idx, bco.version) IN (SELECT * FROM (
        SELECT DISTINCT
          bc.document,
          bc.idx,
          bc.version
        FROM block_content bc
          JOIN block b ON b.document = bc.document AND b.idx = bc.idx AND b.version = bc.version
          JOIN document d ON d.id = b.document AND d.version = b.version
        WHERE d.systemEntityRelation <> 'ATTACHMENT'
              AND d.systemEntityRelation <> 'NOTE'
              AND b.documentType <> 'COLLECTION'
              AND b.documentType <> 'STORY'
              AND (bc.content LIKE "%</div>%"
                   OR bc.content LIKE "%<br>%"
                   OR bc.content LIKE "%</font>%"
                   OR bc.content LIKE "%</p>%"
                   OR bc.content LIKE "%</b>%"
                   OR bc.content LIKE "%</span>%")) t);


UPDATE block_content bco
SET textType = 'HTML'
WHERE (bco.document, bco.idx, bco.version)
      IN (SELECT document, idx, version
          FROM (
                 SELECT DISTINCT
                   bc.document,
                   bc.idx,
                   bc.version
                 FROM block_content bc
                   JOIN block b ON b.document = bc.document AND b.idx = bc.idx AND b.version = bc.version
                   JOIN document d ON d.id = b.document AND d.version = b.version
                   JOIN story s ON d.systemEntity = s.id
                   JOIN document dans ON dans.systemEntity = s.id
                   JOIN answerSet ans ON ans.id = dans.id
                   JOIN questionnaire q ON q.id = ans.questionnaire
                   JOIN document qs ON qs.systemEntity = q.id AND qs.systemEntityRelation = 'SURVEY'
                   JOIN block_content bcq ON bcq.document = qs.id AND bcq.textType = 'HTML'
                   JOIN block bq ON bq.document = qs.id AND bq.idx = bcq.idx AND bq.version = bcq.version AND
                                    bq.documentType NOT IN ('COLLECTION', 'STORY')
                 WHERE d.systemEntityRelation <> 'ATTACHMENT'
                       AND d.systemEntityRelation <> 'NOTE'
                       AND b.documentType <> 'COLLECTION'
                       AND b.documentType <> 'STORY'
                       AND bc.textType IS NULL) t);

UPDATE block_content bco
SET textType = 'PLAIN'
WHERE (bco.document, bco.idx, bco.version)
      IN (SELECT document, idx, version
          FROM (
                 SELECT DISTINCT
                   bc.document,
                   bc.idx,
                   bc.version
                 FROM block_content bc
                   JOIN block b ON b.document = bc.document AND b.idx = bc.idx AND b.version = bc.version
                   JOIN document d ON d.id = b.document AND d.version = b.version
                   JOIN story s ON d.systemEntity = s.id
                   JOIN document dans ON dans.systemEntity = s.id
                   JOIN answerSet ans ON ans.id = dans.id
                   JOIN questionnaire q ON q.id = ans.questionnaire
                   JOIN document qs ON qs.systemEntity = q.id AND qs.systemEntityRelation = 'SURVEY'
                   JOIN block_content bcq ON bcq.document = qs.id AND bcq.textType = 'PLAIN'
                   JOIN block bq ON bq.document = qs.id AND bq.idx = bcq.idx AND bq.version = bcq.version AND
                                    bq.documentType NOT IN ('COLLECTION', 'STORY')
                 WHERE d.systemEntityRelation <> 'ATTACHMENT'
                       AND d.systemEntityRelation <> 'NOTE'
                       AND b.documentType <> 'COLLECTION'
                       AND b.documentType <> 'STORY'
                       AND bc.textType IS NULL) t);
