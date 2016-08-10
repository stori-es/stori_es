#!/bin/bash

MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/aws-settings.prop

echo "This scirpt is not yet complete. See TASK-1479. It may work"
echo "partially if you remove the 'exit' after this warning."
exit 100

MYSQL='mysql -u $LOCAL_DB_USER stories'

set -f; IFS=$'\n'
ORG_DATA=($($MYSQL -BNe "SELECT name, id FROM organization" | sed 's/\t/:/g'))
set +f; unset IFS

for LINE in "${ORG_DATA[@]}"; do
    ORG_NAME=`echo $LINE | awk -F: '{print $1}'`
    ORG_ID=`echo $LINE | awk -F: '{print $2}'`
    echo -n "Remove '$ORG_NAME' ($ORG_ID)? (y|N)? "
    read ANSWER
    
    case "$ANSWER" in
	Y*|y*)
	    echo "Removing '$ORG_NAME'..."

	    echo -e "\tremoving Responses..."
	    $MYSQL -e "DELETE a.* FROM answerSet aSet JOIN questionnaire q ON aSet.questionnaire=q.id JOIN systemEntity qe ON qe.id=q.id JOIN answer a ON a.answerSet=aSet.id WHERE qe.owner=$ORG_ID"

	    echo -e "\tremoving collections/questionnaires..."
	    $MYSQL -e "DELETE b.* FROM questionOptions b JOIN collection c ON c.id=b.questionnaire JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE a.* FROM answerSet a JOIN questionnaire q ON a.questionnaire=q.id JOIN systemEntity qe ON qe.id=q.id WHERE qe.owner=$ORG_ID"
	    $MYSQL -e "DELETE b.* FROM block b JOIN collection c ON c.id=b.questionnaire JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE b.* FROM block b JOIN collection c ON c.id=b.questionnaire JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE b.* FROM content b JOIN collection c ON c.id=b.questionnaire JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE cs.* FROM collection_sources cs JOIN questionnaire q ON q.id=cs.sourceQuestionnaire JOIN systemEntity qe ON q.id=qe.id WHERE qe.owner=$ORG_ID"
	    $MYSQL -e "DELETE q.* FROM questionnaire q JOIN systemEntity qe ON q.id=qe.id WHERE qe.owner=$ORG_ID"
	    $MYSQL -e "DELETE cs.* FROM collection_sources cs JOIN collection c ON c.id=cs.targetCollection JOIN systemEntity ce ON c.id=ce.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE cs.* FROM collection c JOIN collection_story cs ON c.id=cs.collection JOIN systemEntity ce ON c.id=ce.id WHERE ce.owner=$ORG_ID"
	    $MYSQL -e "DELETE c.* FROM collection c JOIN systemEntity ce ON ce.id=c.id WHERE ce.owner=$ORG_ID"

	    echo "\tremoving newly orphanned Stories..."
	    $MYSQL -e "DELETE s.* FROM story s left join collection_story cs ON cs.story=s.id join systemEntity ce on cs.collection=ce.id where cs.story is null and ce.owner=$ORG_ID"

	    echo "\tremoving Profiles..."
	    $MYSQL -e "delete from profile where organization=$ORG_ID"
	    ;;
	*)
	    echo "No action."
	    ;;
    esac
    echo
done