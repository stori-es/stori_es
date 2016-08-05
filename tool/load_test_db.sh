#!/bin/bash

MYSQL='mysql --no-defaults'
DB_USER='cuAdmin'
MAIN_PATH=`dirname $0`/..
TEST_DATA=$MAIN_PATH/sql/testdata.sql
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

# we currently have no password on the DB; this might change, at which
# point we'll probably end up storing the DB in a Java properties fiel
# and reading it in here
# DB_PASS=`cat $PROP_FILE | grep '^jdbc.password' | awk -F= '{print $2}'`

bash $MAIN_PATH/tool/reset_and_load_database.sh $*
echo "Loading test data..."
$MYSQL -u $DB_USER $PASSWORD -h $HOST $DB < $TEST_DATA
