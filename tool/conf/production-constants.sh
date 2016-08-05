source $MAIN_DIR/tool/lib/config-and-keys.sh

# This is the SSH style access <account>@<public DNS> for the EC2
# instance associated with the production beanstalk environment. We
# use the production EC2 because it has more memory available and
# we've run into heap allocation problems on the other servers (which
# are already maxed out running the single AWS instance). In fact, I
# suspect that we should optimize the production web container to
# gobble up more memory; in which case we'll need to leave enough for
# this, or work with a specific staging server, perhaps set up as
# needed. This all could be set up to run from a workstation (has
# different access implications, but those can be addressed). The real
# problem with that is the data transfer can be significant and it's
# much quicker to have everything run within the cloud than to pull
# the data down and then push it back up again. (As happens with the
# Solr indexation; may not be a concern with the geocoding, but
# probably not worth it to separate how the two scripts operate in
# that way.)
ACCOUNT_NAME="bitnami"
SOLR_SERVER=`ensure-solr-host`
REINDEX_ACCOUNT="${ACCOUNT_NAME}@${SOLR_SERVER}"
BITNAMI_NAME="${ACCOUNT_NAME}@${SOLR_SERVER}"
PRODUCTION_SSH_KEY=`ensure-production-key`
TOMCATLIB='/opt/bitnami/apache-solr/lib'

# we need to run checks to verify we have all the necessary context to
# make the necessary remote connections
if [[ x"$TARGET" -ne x"verify" ]] && [[ ! -f $PRODUCTION_SSH_KEY ]]; then
    echo "Could not find production key necessary for SSH access to remote EC2 boxes. Please copy to: " 2>&1
    echo "$PRODUCTION_SSH_KEY" 2>&1
    exit 1
fi
# let's test the key; first for the production server
if [ x"$TARGET" != x"verify" ] && [ x"$TARGET" != x"localdev" ]; then
    # The verify environment does not use 'production' for staging as
    # the integration and testing environments do. Instead it runs the
    # MainIndexer from the Elastic Bamboo instance.
    test_access_to 'production' $REINDEX_ACCOUNT
fi
test_access_to 'bitnami' $BITNAMI_NAME $PRODUCTION_SSH_KEY

# set up the curl targets
SOLR_CORE="$TARGET"
if [[ x"$TARGET" == x"testing" ]]; then
    SOLR_CORE=qa
fi
CURL_BASE="http://$SOLR_SERVER:8983/solr/${SOLR_CORE}-"
BITNAMI_BASE=/opt/bitnami/apache-solr/solr/$SOLR_CORE
