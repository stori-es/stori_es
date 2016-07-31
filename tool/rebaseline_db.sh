#!/bin/bash

MYSQLDUMP=mysqldump
DB_USER='cuAdmin'
# we currently have no password on the DB; this might change, at which
# point we'll probably end up storing the DB in a Java properties fiel
# and reading it in here
# DB_PASS=`cat $PROP_FILE | grep '^jdbc.password' | awk -F= '{print $2}'`

MAIN_PATH=`dirname $0`/..

# determine the file to load; default to the initialization file
if [ -z "$1" ]; then
    DB_FILE=$MAIN_PATH/sql/stories.sql
else
    DB_FILE="$1"
fi

$MYSQLDUMP -u $DB_USER --no-data --skip-triggers stories > $DB_FILE
