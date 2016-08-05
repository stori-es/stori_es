CLEAR_CORES=1 # default true
SKIP_CORE_CHECK=0 # default false

# TODO: to support initializing a new system, add '--initialize'
# option. This would imply 'skip-core-check', test for the core
# directories, if present error out, otherwise run 'solr create -c
# stories -d basic_configs'

# --- Command Line Processing ---
TMP=`getopt --name="$0" -a --longoptions=cores:,no-clear,skip-core-check -o c: -- "$@"`
eval set -- "$TMP"

until [ "$1" == "--" ]; do
    case "$1" in
	# CORES is specified as a space separated list
        --cores|-c)
            CORES="$2";;
	--no-clear)
	    CLEAR_CORES=0;; # false
	--skip-core-check)
	    SKIP_CORE_CHECK=1;; # true
    esac
    shift
done
shift

TARGET=$1

source $MAIN_DIR/tool/lib/solr_lib.sh
source $MAIN_DIR/tool/lib/config-and-keys.sh

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
if [[ $TARGET != 'localdev' ]] && [[ $TARGET != 'integration' ]] && [[ $TARGET != 'testing' ]] && [[ $TARGET != 'production' ]] && [[ $TARGET != 'verify' ]]; then
    echo "Unrecognized target environment: $TARGET" 2>&1
    exit 1
fi

# If no 'CORES' specified, then
if [ -z "$CORES" ]; then # we default to all cores
    CORES="collections stories people"
fi
CORES_ARRAY=($CORES)
# --- END: Command Line Processing ---

if [[ x"$TARGET" != x"localdev" ]]; then
    source $MAIN_DIR/tool/conf/production-constants.sh

    # In a previous version, we checked that the user was on the
    # expected branch. With the current branch names, that's not
    # possible to easily do for all branches. It was a good idea
    # though.
else
    CURL_BASE=http://localhost:8983/solr/
fi

if [[ $TARGET == "localdev" ]]; then
    SOLR_BASE=/home/$USER/solr_tomcat/apache-solr-config
else
    SOLR_BASE=$BITNAMI_BASE
fi

# First, we check for unitialized cores.
if [ $SKIP_CORE_CHECK -eq 0 ]; then
    for i in "${CORES_ARRAY[@]}"; do
	# Previous version tried to do some logic here that would set
	# up cores if they were not already set up. That's nice, but
	# it always acted locally so it's hard to see how it worked
	# for anything but localdev. Now, we just do the test and
	# leave updates as a manual operation.
	test_curl $i
    done
fi
