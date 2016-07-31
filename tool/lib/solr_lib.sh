# Bash functions to support Solr operations.

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
    RESULT=`curl -sL -w "%{http_code}\\n" $URL -o /dev/null`
    EXIT_CODE=$?
    test_exit_and_http_result $EXIT_CODE $RESULT $URL
}

# Used in the 'production-constants.sh' (which maybe should be
# converted to pure var settings).
function test_access_to() {
    TEST_DESCRIPTION="$1"
    TEST_TARGET="$2"
    if [ $# -ge 3 ]; then
	TEST_SSH_KEY="$3"
    else
	TEST_SSH_KEY="$PRODUCTION_SSH_KEY"
    fi

    RESULT=`ssh -oStrictHostKeyChecking=no -i $TEST_SSH_KEY $TEST_TARGET 'echo test'`
    EXIT_CODE=$?
    if [[ $EXIT_CODE -ne 0 ]]; then
       echo -e "\nError executing SSH test ('$RESULT'/$EXIT_CODE):"
       echo "ssh -oStrictHostKeyChecking=no -i $TEST_SSH_KEY $TEST_TARGET 'echo test'"
       echo "Possibly bad production server name: $TEST_TARGET."
       exit 1
    fi
    if [[ x"$RESULT" != x"test" ]]; then
       echo -e "\nCould not confirm access to production server at $TEST_TARGET." 2>&1
       exit 1
    fi
    echo "Done!"
}

function files_same() {
    FILE1="$1"
    FILE2="$2"

    diff -q $FILE1 $FILE2 > /dev/null
    return $?
}

function restart_solr_prod_necessary {
    SOLR_WORKING_DIR=$HOME/.solr-working-dir
    rm -rf $WORKING_DIR || (echo "Could not reset Solr working directory '$SOLR_WORKING_DIR'"; exit 1)
    mkdir $SOLR_WORKING_DIR
    cd $SOLR_WORKING_DIR

    scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME:$BITNAMI_BASE/solr.xml ./solr.xml
    # Note we compare with 'solr-production.xml'.
    if ! files_same ./solr.xml $MAIN_DIR/sql/solr-production.xml; then
	return 0
    fi

    for i in "${CORES_ARRAY[@]}"; do
	# Core specific schema.
	scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME:$BITNAMI_BASE/$i/conf/schema.xml ./schema-${i}.xml
	if ! files_same ./schema-${i}.xml $MAIN_DIR/sql/schema-${i}.xml; then
	    return 0
	fi
	# Core specific solrconfig.
	scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME:$BITNAMI_BASE/$i/conf/solrconfig.xml ./solrconfig-${i}.xml
	if ! files_same ./solrconfig-${i}.xml $MAIN_DIR/sql/solrconfig-${i}.xml; then
	    return 0
	fi
	# Note the 'synonyms.txt' and 'index_synonyms.txt' are the
	# same on our side for all cores.
	# Core specific synonyms.
	scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME:$BITNAMI_BASE/$i/conf/synonyms.txt ./synonyms-${i}.txt
	if ! files_same ./synonyms-${i}.txt $MAIN_DIR/sql/synonyms.txt; then
	    return 0
	fi
	# Core specific index synonyms.
	scp -oStrictHostKeyChecking=no -i $PRODUCTION_SSH_KEY $BITNAMI_NAME:$BITNAMI_BASE/$i/conf/index_synonyms.txt ./index_synonyms-${i}.txt
	if ! files_same ./index_synonyms-${i}.txt $MAIN_DIR/sql/index_synonyms.txt; then
	    return 0
	fi
    done

    # If we get to this point, all configuration files match, so
    # there's no need to update and restart Solr.
    return 1
}