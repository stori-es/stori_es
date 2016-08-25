#!/bin/bash
MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/aws-settings.prop
 
# Detect paths
MYSQL=$(which mysql)
MYSQLDUMP=$(which mysqldump)
GREP=$(which grep)
ECHO=$(which echo)
RM=$(which rm)
AWK=$(which awk)

#tmp file with stories tables dump script
TEST_DUMP_DATABASE_TABLES='test_dump_database_tables.sql';
MAIN_PATH=`dirname $0`/..

MYSQL="$MYSQL -p -h $MYSQL_STAGING"
MYSQLDUMP="$MYSQLDUMP -p -h $MYSQL_STAGING"

# make sure we can connect to server
$MYSQL -u $TEST_DB_USER -e "use $VERIFY_DB"  &>/dev/null
if [ $? -ne 0 ]
then
	$ECHO "Error - Cannot connect to mysql server using given username or database does not exits!"
	exit 1
fi

#check how many tables are into database
TABLES_COUNT=$($MYSQL -u $TEST_DB_USER $VERIFY_DB -e "SELECT count(*) from information_schema.tables WHERE table_schema = '$VERIFY_DB';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT "tables detected into '$VERIFY_DB' database"

#remove file if exist
if [ -e $TEST_DUMP_DATABASE_TABLES ]; then
    $RM $TEST_DUMP_DATABASE_TABLES
fi

#creates the script that deletes all tables 
$ECHO 'SET FOREIGN_KEY_CHECKS = 0;' >> $TEST_DUMP_DATABASE_TABLES
$ECHO "USE $VERIFY_DB" >> $TEST_DUMP_DATABASE_TABLES
$MYSQLDUMP -u $TEST_DB_USER --add-drop-table --no-data $VERIFY_DB | $GREP ^DROP >> $TEST_DUMP_DATABASE_TABLES
$ECHO 'SET FOREIGN_KEY_CHECKS = 1;' >> $TEST_DUMP_DATABASE_TABLES

#executes the script
$MYSQL -u $TEST_DB_USER $MBD < $TEST_DUMP_DATABASE_TABLES

#delete the tmp file that stores the script
$RM $TEST_DUMP_DATABASE_TABLES

#check if database is empty
TABLES_COUNT_AFTER_DELETE=$($MYSQL -u $TEST_DB_USER $VERIFY_DB -e "SELECT count(*) from information_schema.tables WHERE table_schema = '$VERIFY_DB';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT_AFTER_DELETE "tables detected into $VERIFY_DB database after delete tables"

if [ $TABLES_COUNT_AFTER_DELETE -gt 0 ]; then 
    $ECHO 'Error deleting tables, still pending to delete ' $TABLES_COUNT_AFTER_DELETE ' tables:'
    TABLES=$($MYSQL -u $TEST_DB_USER $VERIFY_DB -e 'show tables' | $AWK '{ print $1}' | $GREP -v '^Tables' )
    
    for t in $TABLES; do
	$ECHO "Missing deleting $t table from $VERIFY_DB database..."
    done

    #load test database, before it will be a nightmare for developers
#    exec $MAIN_PATH/tool/load_test_db.sh
   
    #return error
    exit 1
else

    #load test database, before it will be a nightmare for developers
#    exec $MAIN_PATH/tool/load_test_db.sh

    #return everything was OK
    exit 0
fi
 
