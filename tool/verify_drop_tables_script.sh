#!/bin/bash
MUSER="cuAdmin"
MDB="stories"
 
# Detect paths
MYSQL=$(which mysql)
MYSQLDUMP=$(which mysqldump)
GREP=$(which grep)
ECHO=$(which echo)
RM=$(which rm)
AWK=$(which awk)

#tmp file with stories tables dump script
TEST_DUMP_DATABASE_TABLES='clean_data_base.sql';
MAIN_PATH=`dirname $0`/..

# make sure we can connect to server
$MYSQL -u $MUSER -e "use $MDB"  &>/dev/null
if [ $? -ne 0 ]
then
	$ECHO "Error - Cannot connect to mysql server using given username or database does not exits!"
	exit 1
fi

#check how many tables are into database
TABLES_COUNT=$($MYSQL -u $MUSER $MDB -e "SELECT count(*) from information_schema.tables WHERE table_schema = 'stories';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT 'tables detected into stories database'

#executes the script
$MYSQL -u $MUSER -D $MDB < $MAIN_PATH/tool/$TEST_DUMP_DATABASE_TABLES 

#check if database is empty
TABLES_COUNT_AFTER_DELETE=$($MYSQL -u $MUSER $MDB -e "SELECT count(*) from information_schema.tables WHERE table_schema = 'stories';" | $AWK '{print $1}' | $GREP -v '^count')
$ECHO $TABLES_COUNT_AFTER_DELETE 'tables detected into stories database after delete tables'

#clean script creates a table to save all script to be executed, so at least we have one more table
if [ $TABLES_COUNT_AFTER_DELETE -gt 1 ]; then 
    $ECHO 'Error deleting tables, still pending to delete ' $TABLES_COUNT_AFTER_DELETE ' tables:'
    TABLES=$($MYSQL -u $MUSER $MDB -e 'show tables' | $AWK '{ print $1}' | $GREP -v '^Tables' )
    
    for t in $TABLES; do
	$ECHO "Missing deleting $t table from $MDB database..."
    done

    #load test database, before it will be a nightmare for developers
    exec $MAIN_PATH/tool/load_test_db.sh
   
    #return error
    exit 1
else

    #load test database, before it will be a nightmare for developers
    exec $MAIN_PATH/tool/load_test_db.sh

    #return everything was OK
    exit 0
fi
 

