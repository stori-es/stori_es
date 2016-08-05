MYSQL=mysql
DB_USER='cuAdmin'

# first update the DB version
SHA_VERSION=`git rev-parse HEAD`
$MYSQL -u $DB_USER -e "DELETE FROM stories.version; INSERT INTO stories.version VALUES('$SHA_VERSION')"
