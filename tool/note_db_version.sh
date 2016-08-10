MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/aws-settings.prop

MYSQL=mysql

# first update the DB version
SHA_VERSION=`git rev-parse HEAD`
$MYSQL -u $LOCAL_DB_USER -e "DELETE FROM stories.version; INSERT INTO stories.version VALUES('$SHA_VERSION')"
