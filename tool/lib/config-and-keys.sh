
function ensure-production-key() {
    KEY_PATH=$HOME/production-key.pem
    if [ x"$TARGET" == x"verify" ]; then
	if [ -f /mnt/bamboo-ebs/SolrBamboo.key ]; then
	    KEY_PATH="/mnt/bamboo-ebs/SolrBamboo.key"
	else
	    KEY_PATH="$MAIN_DIR/SolrBamboo.key"
	fi
    fi

    if [ ! -f $KEY_PATH ]; then
	echo "No production key found at '$KEY_PATH'." >&2
	exit 1
    fi
    echo $KEY_PATH
}

function ensure-db-password() {
    if [ ! -f $HOME/production-creds.sh ]; then
	echo "No production credentials file found at '$HOME/production-creds.sh'." >&2
	exit 1
    fi
    source $HOME/production-creds.sh
    echo $PRODUCTION_DB_PASSWORD
}

function ensure-solr-host() {
    if [ ! -f $HOME/production-creds.sh ]; then
	echo "No production credentials file found at '$HOME/production-creds.sh'." >&2
	exit 1
    fi
    source $HOME/production-creds.sh
    echo $PRODUCTION_SOLR_HOST
}
