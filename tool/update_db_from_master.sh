#!/bin/bash

MAIN_PATH=`dirname $0`/..

if [ ! -f $MAIN_PATH/.targetBranch ]; then
    echo "did not find target branch marker; bailing out" 1>&2
    exit 1
fi

TARGET_BRANCH=`cat $MAIN_PATH/.targetBranch`

cd $MAIN_PATH # make sure we're in the git repo
git checkout master sql # grab the master DB
./tool/load_test_db.sh
# now, let's figure out the update scripts to run
UPDATE_SCRIPTS=`git diff --summary master..$TARGET_BRANCH | grep '\screate' | grep ' update/' | cut -d' ' -f5`

echo $UPDATE_SCRIPTS

for SCRIPT in "$UPDATE_SCRIPTS"; do
    echo -n "Found script $SCRIPT... "
    # test type
    if [ ${SCRIPT:$((${#FOO} - 3))} == 'sql' ]; then
	echo -n "running as sql... "
	MYSQL='mysql --no-defaults'
	DB_USER='cuAdmin'
	$MYSQL -u $DB_USER stories < $SCRIPT
	echo "done."
    else
	echo "Could not determine update script type. Bailing out." 1>&2
	exit 1
    fi
done
