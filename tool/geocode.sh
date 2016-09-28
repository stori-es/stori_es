#!/bin/bash

TARGET=$1

# regularize the target with old synonyms
if [[ x"$TARGET" == x"" ]]; then
    TARGET=localdev
elif [[ x"$TARGET" == x"x" ]]; then
    TARGET=integration
elif [[ x"$TARGET" == x"y" ]]; then
    TARGET=testing
elif [[ x"$TARGET" == x"z" ]]; then
    TARGET=production
fi

# check valid target
if [[ $TARGET != 'localdev' ]] && [[ $TARGET != 'integration' ]] && [[ $TARGET != 'testing' ]] && [[ $TARGET != 'production' ]]; then
    echo "Unrecognized target environment: $TARGET" 2>&1
    exit 1
fi

function test_access_to() {
    TEST_DESCRIPTION="$1"
    TEST_TARGET="$2"

    echo -en "About to test access to $TEST_DESCRIPTION server.\n You may be asked to confirm the SSH fingerprint... "
    RESULT=`ssh -i $PRODUCTION_SSH_KEY $TEST_TARGET 'echo test'`
    EXIT_CODE=$?
    if [[ $EXIT_CODE -ne 0 ]]; then
	echo -e "\nError executing SSH test; possibly bad production server name: $TEST_TARGET."
	exit 1
    fi
    if [[ x"$RESULT" != x"test" ]]; then
	echo -e "\nCould not confirm access to production server at $TEST_TARGET." 2>&1
	exit 1
    fi
    echo "Done!"
}

function test_exit_and_http_result() {
    EXIT_CODE="$1"
    RESULT="$2"
    URL=$3
    if [[ $EXIT_CODE -ne 0 ]]; then
	echo "Unexpected error code from curl test to '$URL'; $EXIT_CODE" 2>&1
	exit 1
    fi
    if [[ $RESULT -ne 200 ]]; then
	echo "Could not connect to '$URL'; $RESULT" 2>&1
	exit 1
    fi
}

function test_curl() {
    CORE="$1"
    URL="${CURL_BASE}${CORE}/admin/ping"
    echo -e "\ncurl -sL -w '%{http_code}\\n' $URL -o /dev/null\n"
    RESULT=`curl -sL -w "%{http_code}\\n" $URL -o /dev/null`
    EXIT_CODE=$?
    echo "EC: '$EXIT_CODE'; RESULT: '$RESULT'; URL: '$URL'"
    test_exit_and_http_result $EXIT_CODE $RESULT $URL
}

source `dirname $0`/../aws-settings.prop

if [[ x"$TARGET" != x"localdev" ]]; then
    source `dirname $0`/conf/production-constants.sh

    # check that we are on the correct branch for the target
    BRANCH=$TARGET
    if [[ $TARGET == "integration" ]]; then
	BRANCH='master'
    fi
    BRANCH_CHECK=`git branch | grep '*' | cut -d' ' -f2`
#    if [[ x"$BRANCH" != x"$BRANCH_CHECK" ]]; then
#	echo "Unexpected branch; expected '$BRANCH', but '$BRANCH_CHECK'." 2>&1
#	exit 1
#    fi
else
    CURL_BASE=http://localhost:8983/solr/
fi

# and then run the re-index
if [[ $TARGET == 'localdev' ]]; then
    CLASSES=/home/$USER/main/site/WEB-INF/classes/
    LIBDIR=/home/$USER/main/site/WEB-INF/lib
    TOMCATLIB=/home/$USER/tomcat/lib
    java -classpath "$CLASSES:$LIBDIR/*:$TOMCATLIB/*" MainGeoCoder
else
    # in the remote context, we need to update the library first
    cd $HOME/main/site/WEB-INF || (echo "Could not change directory to local WEB-INF. Finish manually." 2>&1; exit 1)
    rm classes.tar.gz
    tar czf classes.tar.gz classes || (echo "Could not generate tar file. Finish manually." 2>&1; exit 1)
    scp -i `ensure-production-key` classes.tar.gz $REINDEX_ACCOUNT: || (echo "Could not copy classes.tar.gz. Finish manually." 2>&1; exit 1)
    ssh -i `ensure-production-key` $REINDEX_ACCOUNT 'cd $HOME; rm -rf code; mkdir code; cd $HOME/code; tar xzf $HOME/classes.tar.gz' || (echo "Could not unpack classes.tar.gz on server. Finish manually." 2>&1; exit 1)
    ssh -i `ensure-production-key` $REINDEX_ACCOUNT "mkdir -p ~/lib/$TARGET" || (echo "Could not create remote lib dir '$HOME/lib/$TARGET'" 2>&1; exit 1);
    rsync --delete -rvz -e "ssh -i `ensure-production-key`" $HOME/main/site/WEB-INF/lib/* $REINDEX_ACCOUNT:lib/$TARGET
    LIBDIR="/home/ec2-user/lib/$TARGET"
    # TOMCATLIB defined by 'production-constants.sh' included above
    CLASSES='/home/ec2-user/code/classes/'
    if [[ $TARGET == 'production' ]]; then
	DB_STRING="jdbc:mysql://${MYSQL_PRODUCTION}/$PROD_DB?user=$PROD_DB_USER"
    elif [[ $TARGET == 'testing' ]]; then
	DB_STRING="jdbc:mysql://${MYSQL_STAGING}/$TEST_DB?user=$TEST_DB_USER"
    else
	DB_STRING="jdbc:mysql://${MYSQL_STAGING}/$INT_DB?user=$TEST_DB_USER"
    fi
    DB_STRING="${DB_STRING}&password=`ensure-db-password`"
    SOLR_STRING="http://$SOLR_SERVER:8983/solr/$SOLR_CORE"
    GEOCODERCOMMAND="java -classpath '$CLASSES:$LIBDIR/*:$TOMCATLIB/*' -DJDBC_CONNECTION_STRING='$DB_STRING' -DPARAM1='$SOLR_STRING' MainGeoCoder"
    ssh -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "${GEOCODERCOMMAND}" || (echo "Could not execute remote geocoding service. Finish manually." 2>&1; exit 1)
fi
