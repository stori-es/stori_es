#!/bin/bash

clear

MAIN_PATH=`dirname $0`/..
MYSQL='mysql --no-defaults'
DB_USER='cuAdmin'
MDB="stories"
GREP=$(which grep)
AWK=$(which awk)

FILES=$MAIN_PATH/dashboard/src/main/webapp/WEB-INF/schemaUpdates

echo "...Stories db Update Manually... Started..."

# make sure we can connect to server
$MYSQL -u $DB_USER -e "use $MDB"  &>/dev/null
if [ $? -ne 0 ] 
then
	$ECHO "Error - Cannot connect to mysql server using given username or database does not exits!"
	exit 1
fi
#$FILES
for f in  $( find $FILES -type f -name '*' | sort -n)
do
  echo "Processing file $f"
  FILENAME=${f##*/}
  IS_EXECUTED=$($MYSQL -u $DB_USER -e "SELECT count(*) FROM dbUpdate db WHERE LOWER(db.scriptName) = LOWER('"$FILENAME"')" $MDB | $AWK '{print $1}' | $GREP -v '^count')
  if [ $IS_EXECUTED = 0 ]; then 
  	$MYSQL -u $DB_USER $MDB < $f
  	if [ $? = 0 ] 
  	then
   		echo "Script Executed Successful " $f
   		$MYSQL -u $DB_USER -e "insert into dbUpdate (scriptName) values ('"$FILENAME"')" $MDB
   	else
   		echo "Error Running script " $f
	fi
  fi
done

echo "...Stories db Update Manually... Finished..."
