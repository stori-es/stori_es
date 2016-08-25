#!/bin/bash

MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/aws-settings.prop

MYSQL='mysql --no-defaults'
# we currently have no password on the DB; this might change, at which
# point we'll probably end up storing the DB in a Java properties fiel
# and reading it in here
# DB_PASS=`cat $PROP_FILE | grep '^jdbc.password' | awk -F= '{print $2}'`
MAIN_PATH=`dirname $0`/..
DB_FILE=$MAIN_PATH/sql/stories.sql

echo $*;

if [ $# -eq 0 ] || [ $# -eq 1 ]; then
    if [ $# -eq 1 ]; then
	DB_FILE="$1"
    fi
    echo "$MYSQL -u $LOCAL_DB_USER stories < $DB_FILE"
    $MYSQL -u $LOCAL_DB_USER stories < $DB_FILE
    MYSQL_EXIT=$?
    if [ $MYSQL_EXIT -eq 0 ]; then
	echo "Loading triggers..."
	$MYSQL -u $LOCAL_DB_USER stories < $MAIN_PATH/sql/create_triggers.sql
    fi
else
    # determine the file to load; default to the initialization file
    if [ x"$1" == x"" ]; then
	DB=stories
    else
	DB="$1"
    fi
    if [ x"$2" == x"" ]; then
	HOST="localhost"
    else
	HOST="$2"
    fi
    if [ x"$3" != x"" ]; then
	PASSWORD="--password=$3"
    fi
    $MYSQL -u $LOCAL_DB_USER $PASSWORD -h $HOST $DB < $DB_FILE
    MYSQL_EXIT=$?
    if [ $MYSQL_EXIT -eq 0 ]; then
	$MYSQL -u $LOCAL_DB_USER $PASSWORD -h $HOST $DB < $MAIN_PATH/sql/create_triggers.sql
    fi
fi

# if we add password to LOCAL_DB_USER, add --password=$DB_PASS' here
if [ $MYSQL_EXIT -ne 0 ]; then
    echo "DB load/reset failed with the following message from mysql"
    exit 1;
fi
