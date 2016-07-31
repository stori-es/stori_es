#!/bin/bash
MUSER="cuAdmin"
MDB="cidb"
 
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

MHOST="xydbinstance.c3tp746p8xwp.us-east-1.rds.amazonaws.com"

MYSQL="$MYSQL -p -h $MHOST"
MYSQLDUMP="$MYSQLDUMP -p -h $MHOST"

# make sure we can connect to server
$MYSQL -u $MUSER -e "use $MDB"  &>/dev/null
if [ $? -ne 0 ]
then
	$ECHO "Error - Cannot connect to mysql server using given username or database does not exits!"
	exit 1
fi

#check how many tables are into database
TABLES_COUNT=$($MYSQL -u $MUSER $MDB -e "SELECT count(*) from information_schema.tables WHERE table_schema = '$MDB';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT "tables detected into '$MDB' database"

#remove file if exist
if [ -e $TEST_DUMP_DATABASE_TABLES ]; then
    $RM $TEST_DUMP_DATABASE_TABLES
fi

#creates the script that deletes all tables 
$ECHO 'SET FOREIGN_KEY_CHECKS = 0;' >> $TEST_DUMP_DATABASE_TABLES
$ECHO "USE $MDB" >> $TEST_DUMP_DATABASE_TABLES
$MYSQLDUMP -u $MUSER --add-drop-table --no-data $MDB | $GREP ^DROP >> $TEST_DUMP_DATABASE_TABLES
$ECHO 'SET FOREIGN_KEY_CHECKS = 1;' >> $TEST_DUMP_DATABASE_TABLES

#executes the script
$MYSQL -u $MUSER $MBD < $TEST_DUMP_DATABASE_TABLES

#delete the tmp file that stores the script
$RM $TEST_DUMP_DATABASE_TABLES

#check if database is empty
TABLES_COUNT_AFTER_DELETE=$($MYSQL -u $MUSER $MDB -e "SELECT count(*) from information_schema.tables WHERE table_schema = '$MDB';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT_AFTER_DELETE "tables detected into $MDB database after delete tables"

if [ $TABLES_COUNT_AFTER_DELETE -gt 0 ]; then 
    $ECHO 'Error deleting tables, still pending to delete ' $TABLES_COUNT_AFTER_DELETE ' tables:'
    TABLES=$($MYSQL -u $MUSER $MDB -e 'show tables' | $AWK '{ print $1}' | $GREP -v '^Tables' )
    
    for t in $TABLES; do
	$ECHO "Missing deleting $t table from $MDB database..."
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
 
