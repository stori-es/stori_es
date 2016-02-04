UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "EMAIL WORK"
WHERE type = "EMAIL" AND c.opt = "Work";
UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "EMAIL"
WHERE type = "EMAIL" AND c.opt = "Home";
UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "EMAIL OTHER"
WHERE type = "EMAIL" AND c.opt = "Other";

UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "PHONE WORK"
WHERE type = "PHONE" AND c.opt = "Work";
UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "PHONE"
WHERE type = "PHONE" AND c.opt = "Home";
UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "PHONE OTHER"
WHERE type = "PHONE" AND c.opt = "Other";
UPDATE question q JOIN contactContent c ON c.questionnaire = q.questionnaire AND c.idx = q.idx
SET label = "PHONE MOBILE"
WHERE type = "PHONE" AND c.opt = "Mobile";
