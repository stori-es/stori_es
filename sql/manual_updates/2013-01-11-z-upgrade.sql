DELIMITER //

CREATE PROCEDURE updateStoryTellers()
BEGIN
  DECLARE storyIdX, answerSetIdX INT;
  
  DECLARE done INT DEFAULT 0;
  -- select all the user who don't currently have the specified grant
  DECLARE cur CURSOR FOR
    SELECT id, answerSet FROM story WHERE owner IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;
  
  REPEAT
    FETCH cur INTO storyIdX, answerSetIdX;
    IF NOT done THEN
    BEGIN
      SET @givenName=(SELECT reportValue FROM answer WHERE answerSet=answerSetIdX AND displayValue LIKE 'First Name%');
      SET @surname=(SELECT reportValue FROM answer WHERE answerSet=answerSetIdX AND displayValue LIKE 'Last Name%');
      SET @personId=(SELECT MAX(id) FROM person WHERE givenName=@givenName AND surname=@surname);
      UPDATE story SET owner=@personId WHERE id=storyIdX;
    END;
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL updateStoryTellers();

DROP PROCEDURE updateStoryTellers;

INSERT INTO dbUpdate VALUES ('2012-11-01-storyteller-fix.sql');-- after the answer set connected to stories run the
-- 2012-10-30-b-systmo-1415.sql (sic), there are 25 stories without
-- connection to answer sets; 23 of these are tests and the remaining
-- two are the same story... I have not found any hook to tie these to
-- an answer set... so in the interest of progress, I've saved the
-- details of the lost story in case we can recover
-- DELETE FROM documentText WHERE documentId IN (select id from document where systemEntity in (select id from story where answerSet is null));
-- delete from documentContributor where document in (select id from document where systemEntity in (select id from story where answerSet is null));
-- DELETE d.*, e.* FROM document d join entity e on d.id=e.id where d.systemEntity in (select id from story where answerSet is null);
-- DELETE FROM collection_story WHERE story IN (SELECT id FROM story WHERE answerSet IS NULL);
-- DELETE FROM story WHERE answerSet IS NULL;

-- this is no longer necessary
ALTER TABLE answerSet DROP FOREIGN KEY fk_answerSet_systementity;

-- for those answer sets where we can connect to a story, we'll use
-- the story owner for the answer set respondent
UPDATE answerSet aSet
  JOIN story s ON s.answerSet=aSet.id
  SET aSet.respondent=s.owner
  WHERE aSet.respondent IS NULL AND s.owner IS NOT NULL;

-- for the remaining answer sets, don't have story.owner, so we have
-- to grab the email from the answer set itself and use it to find the
-- person associated with the email, and then set the respondent to
-- that person
DELIMITER //
CREATE PROCEDURE connectRemainingRespondents()
BEGIN
  DECLARE answerSetIdX, storyIdX INT;

  DECLARE done INT DEFAULT 0;
  DECLARE cur CURSOR FOR
    SELECT aSet.id, s.id 
      FROM answerSet aSet
      JOIN story s ON s.answerSet=aSet.id
      WHERE aSet.respondent IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;

  REPEAT
    FETCH cur INTO answerSetIdX, storyIdX;
    IF NOT done THEN
      BEGIN
      SET @emailX = (SELECT ans.reportValue FROM answerSet aSet JOIN answer ans ON aSet.id=ans.answerSet JOIN question q ON q.questionnaire=aSet.questionnaire AND q.label=ans.label JOIN formElement f ON q.questionnaire=f.questionnaire AND q.idx=f.idx WHERE f.standardMeaning='EMAIL' AND aSet.id=answerSetIdX);
      SET @pId = (SELECT p.id FROM person p JOIN contact c ON c.entityId=p.id WHERE c.value=@emailX);
      UPDATE answerSet SET respondent=@pId WHERE id=answerSetIdX;
      UPDATE story SET owner=@pId WHERE id=storyIdX;
      END;
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END//

DELIMITER ;

CALL connectRemainingRespondents();
DROP PROCEDURE connectRemainingRespondents;

-- there is one answer set; we just have to get rid of it; it doesn't actually have any data
delete from collection_story where story=(select id from story where answerSet=482075);
delete from story where answerSet=482075;
delete from answerSet where respondent is NULL and id=482075;

ALTER TABLE answerSet MODIFY respondent INT(11) NOT NULL;

-- allright, now we can go ahead and create the bulk of the documents,
-- but this only works when the story is correctly available
INSERT INTO document (id, primaryAuthor, title, permalink, public, systemEntity, systemEntityRelation)
  SELECT a.id, a.respondent, '', '', FALSE, s.id, 'ANSWER_SET'
    FROM answerSet a
    JOIN story s ON s.answerSet=a.id;

-- now create a story for the remaining answer sets without storys (there are about 96)
DELIMITER //
CREATE PROCEDURE createStories()
BEGIN
  DECLARE answerSetIdX, respondentX INT;
  DECLARE createdX TIMESTAMP;

  DECLARE done INT DEFAULT 0;
  DECLARE cur CURSOR FOR
    SELECT a.id, a.respondent, e.created 
      FROM answerSet a JOIN systemEntity e ON a.id=e.id LEFT JOIN document d on a.id=d.id
      WHERE d.id IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;

  REPEAT
    FETCH cur INTO answerSetIdX, respondentX, createdX;
    IF NOT done THEN
    BEGIN
      INSERT INTO systemEntity (created, lastModified, version, public)
        VALUES (createdX, NULL, 1, FALSE);
      INSERT INTO story (id, owner, permalink, title, defaultRepresentation, published, firstPublished, answerSet, byLine)
        VALUES (LAST_INSERT_ID(), respondentX, MD5(LAST_INSERT_ID()), NULL, NULL, TRUE, createdX, answerSetIdX, '');
      INSERT INTO document (id, primaryAuthor, title, permalink, public, systemEntity, systemEntityRelation)
        VALUES (answerSetIdX, respondentX, '', MD5(LAST_INSERT_ID()), FALSE, LAST_INSERT_ID(), 'ANSWER_SET');
    END;
    END IF;
  UNTIL done END REPEAT;  
END//
DELIMITER ;
CALL createStories();
DROP PROCEDURE createStories;

-- finally, we can do the final DB alteration; AnswerSets are now also Documents
ALTER TABLE answerSet ADD FOREIGN KEY fk_answerSet_systementity (id) REFERENCES document (id);
-- and we drop the specific reference from Story to AnswerSets
ALTER TABLE story DROP FOREIGN KEY fk_story_answerSet, DROP COLUMN answerSet;

alter table person modify column surname varchar(255) null;
alter table document modify column title varchar(255) null;

INSERT INTO dbUpdate VALUES ('2012-11-12-systwo-1415.sql');DELIMITER //

CREATE PROCEDURE updateStoryAuths(IN maskX INT)
BEGIN
  DECLARE orgIdX, storyIdX INT;
  
  DECLARE done INT DEFAULT 0;
  -- select all the user who don't currently have the specified grant
  DECLARE cur CURSOR FOR
    SELECT c.organization, s.id
      FROM story s 
      JOIN collection_story cs ON cs.story=s.id
      JOIN collection c ON cs.collection=c.id
      LEFT JOIN acl_entry acl ON s.id=acl.acl_object_identity AND mask=maskX
      WHERE acl.id IS NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
  OPEN cur;
  
  REPEAT
    FETCH cur INTO orgIdX, storyIdX;
    IF NOT done THEN
      SET @ace_order= 
        (SELECT COALESCE(MAX(ace_order),-1) + 1 FROM acl_entry WHERE acl_object_identity=storyIdX);
      INSERT INTO acl_entry
        (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
       VALUES (storyIdX, @ace_order, orgIdX, maskX, 1, 0, 0);
    END IF;
  UNTIL done END REPEAT;
  CLOSE cur;
END //

DELIMITER ;

CALL updateStoryAuths(1);
CALL updateStoryAuths(2);
CALL updateStoryAuths(16);

DROP PROCEDURE updateStoryAuths;
