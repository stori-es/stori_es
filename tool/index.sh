#!/bin/bash

MAIN_DIR=`dirname $0`
cd $MAIN_DIR
cd ..
MAIN_DIR=`pwd`

source $MAIN_DIR/tool/lib/index-lib.sh
source $MAIN_DIR/aws-settings.prop

# Run the index. The previous index has been cleared and the
# configuration updated as necessary.
if [[ $TARGET == 'localdev' ]] || [[ $TARGET == 'verify' ]]; then
    CLASSES=$MAIN_DIR/dashboard/target/war/WEB-INF/classes/
    LIBDIR=$MAIN_DIR/dashboard/target/war/WEB-INF/lib
    if [[ $TARGET == 'localdev' ]]; then
        TOMCATLIB=/home/$USER/tomcat/lib
        DB_SETTINGS=''
    else
        TOMCATLIB=$MAIN_DIR/tomcatlibs
        DB_STRING="jdbc:mysql://${MYSQL_STAGING}/cidb?user=cuAdmin&password=`ensure-db-password`"
        SOLR_STRING="http://$SOLR_SERVER:8983/solr/$SOLR_CORE"
        DB_SETTINGS="-DJDBC_CONNECTION_STRING='$DB_STRING' -DPARAM1='$SOLR_STRING'"
    fi

    java -classpath "$CLASSES:$LIBDIR/*:$TOMCATLIB/*" $DB_SETTINGS -DskipDbUpdate=true MainIndexer $CORES
else
    # in the remote context, we need to update the library first
    cd $HOME/main/dashboard/target/war/WEB-INF || (echo "Could not change directory to local WEB-INF. Finish manually." 2>&1; exit 1)
    rm classes.tar.gz
    tar czf classes.tar.gz classes || (echo "Could not generate tar file. Finish manually." 2>&1; exit 1)
    scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY classes.tar.gz $REINDEX_ACCOUNT: || (echo "Could not copy classes.tar.gz. Finish manually." 2>&1; exit 1)
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT 'cd $HOME; rm -rf code; mkdir code; cd $HOME/code; tar xzf $HOME/classes.tar.gz' || (echo "Could not unpack classes.tar.gz on server. Finish manually." 2>&1; exit 1)
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "mkdir -p ~/lib/$TARGET" || (echo "Could not create remote lib dir '$HOME/lib/$TARGET'" 2>&1; exit 1);
    rsync --delete -rvz -e "ssh -oStrictHostKeyChecking=no -i `ensure-production-key`" $HOME/main/dashboard/target/war/WEB-INF/lib/* $REINDEX_ACCOUNT:lib/$TARGET
    LIBDIR="/home/ec2-user/lib/$TARGET"
    # TOMCATLIB is defined in 'production-constants.sh' included above
    CLASSES='/home/ec2-user/code/classes/'
    if [[ $TARGET == 'production' ]]; then
	DB_STRING='jdbc:mysql://${MYSQL_PRODUCTION}/zdb?user=zdb'
    elif [[ $TARGET == 'testing' ]]; then
	DB_STRING='jdbc:mysql://${MYSQL_STAGING}/ydb?user=cuAdmin'
    elif [[ $TARGET == 'integration' ]]; then
	DB_STRING='jdbc:mysql://${MYSQL_STAGING}/xdb?user=cuAdmin'
    else # Default to the 'verify' DB as the most volatile / least important.
	DB_STRING='jdbc:mysql://${MYSQL_STAGING}/cidb?user=cuAdmin'
    fi
    DB_STRING="${DB_STRING}&password=`ensure-db-password`"
    SOLR_STRING="http://$SOLR_SERVER:8983/solr/$SOLR_CORE"
    SOLRCOMMAND="java -classpath '$CLASSES:$LIBDIR/*:$TOMCATLIB/*' -DskipDbUpdate=true -DJDBC_CONNECTION_STRING='$DB_STRING' -DPARAM1='$SOLR_STRING' MainIndexer $CORES"
    ssh -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $REINDEX_ACCOUNT "${SOLRCOMMAND}" || (echo "Could not execute remote solr reindex command. Finish manually." 2>&1; exit 1)
fi
