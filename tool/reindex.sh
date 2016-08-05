#!/bin/bash

MYSQL_STAGING=xydbinstance.c3tp746p8xwp.us-east-1.rds.amazonaws.com

SOLR_HOME=$HOME/solr-5.2.1
SOLR_CTL=$SOLR_HOME/bin/solr
SOLR_DATA=$SOLR_HOME/server/solr

MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/tool/lib/index-lib.sh
source $MAIN_DIR/aws-settings.prop

COMMON_PROPERTIES="-DNO_EMAIL=TRUE -Dsys.noGeoCode=TRUE -Dsys.noS3=TRUE"

# It is necessary to restart the serer if the configuration files have
# changed. Because the workstation and production environments are not
# congruent, it is necessary to maintain a branch for 'localdev' and
# another branch for the production environment. In order to simplify
# all this, we don't bother supporting the restart check for the
# localdev environment and always restart it. In production, the check
# is important to avoid unecessary downtime. We make this
# determination before deleting the entries in order to minimize
# downtime.
UPDATE_AND_RESTART='false'
if [[ $TARGET == "localdev" ]] || restart_solr_prod_necessary; then
    UPDATE_AND_RESTART='true'
fi

if [ $CLEAR_CORES == 1 ]; then
    # Clear all current entries.
    echo "Clearing existing Solr DB...";
    for i in "${CORES_ARRAY[@]}"; do
	echo -e "\t${i}... "
	URL="${CURL_BASE}${i}/update?commit=true"
	RESULT=`curl -sL -w "%{http_code}\\n" $URL --data '<delete><query>*:*</query></delete>' -H 'Content-type:text/xml; charset=utf-8' -o /dev/null`
       # EXIT_CODE=$?
       # test_exit_and_http_result $EXIT_CODE $RESULT $URL
    done
    echo "core cleared."
fi

if [[ $UPDATE_AND_RESTART == 'true' ]]; then
    if [[ $TARGET == "localdev" ]]; then
	$SOLR_CTL stop
    else
	ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME 'sudo /opt/bitnami/ctlscript.sh stop solr' || (echo "Remote shutdown failed. MANUAL REPAIR REQUIRED." 2>&1; exit 1)
    fi
    # Update the configeration files.
    if [[ $TARGET == "localdev" ]]; then
	cp $MAIN_DIR/sql/solr.xml $SOLR_DATA/solr.xml
	for i in "${CORES_ARRAY[@]}"; do
	    CORE_CONF=$SOLR_DATA/$i/conf
	    mkdir -p $CORE_CONF
	    cp $MAIN_DIR/sql/solrconfig-${i}.xml $CORE_CONF/solrconfig.xml
	    cp $MAIN_DIR/sql/schema-${i}.xml $CORE_CONF/schema.xml
	    cp $MAIN_DIR/sql/synonyms.txt $MAIN_DIR/sql/index_synonyms.txt $CORE_CONF
	done
    else
	cat $MAIN_DIR/sql/solr.xml | ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME "cat - | sudo -u solr bash -c 'cat - > $BITNAMI_BASE/solr.xml'"
	for i in "${CORES_ARRAY[@]}"; do # btw, it's 'qa', not testing
	    cat $MAIN_DIR/sql/schema-${i}.xml | ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME "cat - | sudo -u solr bash -c 'cat - > $BITNAMI_BASE/$i/conf/schema.xml'"
	    cat $MAIN_DIR/sql/solrconfig-${i}.xml | ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME "cat - | sudo -u solr bash -c 'cat - > $BITNAMI_BASE/$i/conf/solrconfig.xml'"
	    cat $MAIN_DIR/sql/synonyms.txt | ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME "cat - | sudo -u solr bash -c 'cat - > $BITNAMI_BASE/$i/conf/synonyms.txt'"
	    cat $MAIN_DIR/sql/index_synonyms.txt | ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME "cat - | sudo -u solr bash -c 'cat - > $BITNAMI_BASE/$i/conf/index_synonyms.txt'"
	done
    fi

    # Restart the server.
    if [[ $TARGET == 'localdev' ]]; then
	$SOLR_CTL start
    else
	ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME 'sudo /opt/bitnami/ctlscript.sh start solr' || (echo "Remote restart failed. MANUAL REPAIR REQUIRED." 2>&1; exit 1)
    fi
    PING_URL=${CURL_BASE}stories/admin/ping
    echo "Pausing to allow time for Solr to spin up; using test URL: $PING_URL"
    SOLR_UP=false
    SOLR_TRY_COUNT=0
    while [ $SOLR_UP == 'false' ]; do
	if [ $SOLR_TRY_COUNT -gt 60 ]; then # That's 2 minutes
	    echo "WARNING: The solr server appears to hung on restart." >&2
	    exit 2
	fi
	
	RESULT=`curl -sL -w "%{http_code}\\n" $PING_URL -o /dev/null`
	EXIT_CODE=$?
	if [[ $EXIT_CODE -eq 0 ]] && [[ $RESULT -eq 200 ]]; then
	    SOLR_UP='true'
	fi
	
	if [ $SOLR_UP == 'false' ]; then
	    echo -n "."
	    SOLR_TRY_COUNT=$(($SOLR_TRY_COUNT + 1))
	    sleep 2
	fi
    done
fi # Update config and restart server if necessary

# Run the re-index. The previous index has been cleared and the
# configuration updated as necessary.
if [[ $TARGET == 'localdev' ]] || [[ $TARGET == 'verify' ]]; then
    CLASSES=$MAIN_DIR/dashboard/target/war/WEB-INF/classes/
    LIBDIR=$MAIN_DIR/dashboard/target/war/WEB-INF/lib
    # Note that 'TOMCATLIB' is for the indexer, not solr itself.
    if [[ $TARGET == 'localdev' ]]; then
        TOMCATLIB=/home/$USER/tomcat/lib
        DB_SETTINGS=''
    else
	# The location is totally idiomatic; this is just where we
	# last loaded the 'tomcatlibs' which are a copy of libs
	# shipped with Tomcat we need to run the indexer (may be an
	# avoidable dependency, but there you go).
        TOMCATLIB=/mnt/bamboo-ebs/bamboo-agent/build-dir/SYSTHREE-TEST-CTED/tomcatlibs
        DB_STRING="jdbc:mysql://${MYSQL_STAGING}/cidb?user=cuAdmin&password=`ensure-db-password`"
        SOLR_STRING="http://$SOLR_SERVER:8983/solr/$SOLR_CORE"
        DB_SETTINGS="-DJDBC_CONNECTION_STRING='$DB_STRING' -DPARAM1='$SOLR_STRING'"
    fi

    CLASSPATH="-classpath '$CLASSES:$LIBDIR/*:$TOMCATLIB/*'"

    SOLRCOMMAND="nohup java -classpath '$CLASSES:$LIBDIR/*:$TOMCATLIB/*' $DB_SETTINGS -DskipDbUpdate=true $COMMON_PROPERTIES MainIndexer $CORES"
    echo -e "solr command (a):\n$SOLRCOMMAND"
    eval $SOLRCOMMAND
else
    # in the remote context, we need to update the library first
    cd $HOME/main/dashboard/target/war/WEB-INF || (echo "Could not change directory to local WEB-INF. Finish manually." 2>&1; exit 1)
    rm classes.tar.gz
    tar czf classes.tar.gz classes || (echo "Could not generate tar file. Finish manually." 2>&1; exit 1)
    scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY classes.tar.gz $REINDEX_ACCOUNT: || (echo "Could not copy classes.tar.gz. Finish manually." 2>&1; exit 1)
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT 'cd $HOME; rm -rf code; mkdir code; cd $HOME/code; tar xzf $HOME/classes.tar.gz' || (echo "Could not unpack classes.tar.gz on server. Finish manually." 2>&1; exit 1)
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "mkdir -p ~/lib/$TARGET" || (echo "Could not create remote lib dir '$HOME/lib/$TARGET'" 2>&1; exit 1);
    rsync --delete -rvz -e "ssh -oStrictHostKeyChecking=no -i `ensure-production-key`" $HOME/main/dashboard/target/war/WEB-INF/lib/* $REINDEX_ACCOUNT:lib/$TARGET
    LIBDIR="/home/$ACCOUNT_NAME/lib/$TARGET"
    CLASSES="/home/$ACCOUNT_NAME/code/classes/"
    if [[ $TARGET == 'production' ]]; then
	DB_STRING="jdbc:mysql://${MYSQL_PRODUCTION}/zdb?user=zdb"
    elif [[ $TARGET == 'testing' ]]; then
	DB_STRING="jdbc:mysql://${MYSQL_STAGING}/ydb?user=cuAdmin"
    elif [[ $TARGET == 'integration' ]]; then
	DB_STRING="jdbc:mysql://${MYSQL_STAGING}/xdb?user=cuAdmin"
    else # Default to the 'verify' DB as the most volatile / least important.
	DB_STRING="jdbc:mysql://${MYSQL_STAGING}/cidb?user=cuAdmin"
    fi
    DB_STRING="${DB_STRING}&password=`ensure-db-password`"
    SOLR_STRING="http://$SOLR_SERVER:8983/solr/$SOLR_CORE"
    SOLRCOMMAND="nohup java -classpath '$CLASSES:$LIBDIR/*:$TOMCATLIB/*' -DskipDbUpdate=true -DJDBC_CONNECTION_STRING='$DB_STRING' -DPARAM1='$SOLR_STRING' $COMMON_PROPERTIES MainIndexer $CORES"
    echo "solr command (b):"
    echo ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "${SOLRCOMMAND}"
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "${SOLRCOMMAND}" || (echo "Could not execute remote solr reindex command. Finish manually." 2>&1; exit 1)
fi
