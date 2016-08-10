#!/bin/bash

clear

MAIN_PATH=`dirname $0`/..
MYSQL='mysql --no-defaults'
MDB="stories"
GREP=$(which grep)
AWK=$(which awk)
source $MAIN_PATH/aws-settings.prop

TRIGGERS=$MAIN_PATH/site/WEB-INF/schemaUpdates/2012-08-24_systwo-1100-stories_triggers.sql

echo "...Stories db Update Triggers Manually... Started..."

# make sure we can connect to server
$MYSQL -u $LOCAL_DB_USER -e "use $MDB"  &>/dev/null
if [ $? -ne 0 ] 
then
	$ECHO "Error - Cannot connect to mysql server using given username or database does not exits!"
	exit 1
fi

FILENAME=${TRIGGERS##*/}
$MYSQL -u $LOCAL_DB_USER $MDB < $TRIGGERS
if [ $? = 0 ] 
then
	echo "Script Executed Successful " $TRIGGERS
else
	echo "Error Running script " $TRIGGERS
fi

echo "...Stories db Update Triggers Manually... Finished..."
