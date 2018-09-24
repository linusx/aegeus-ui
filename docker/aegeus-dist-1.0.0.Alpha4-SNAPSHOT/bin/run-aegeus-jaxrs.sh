#!/bin/sh

PRG="$0"

# need this for relative symlinks
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG="`dirname "$PRG"`/$link"
  fi
done

HOMEDIR=`dirname $PRG`/..

# Get absolute path of the HOMEDIR
CURDIR=`pwd`
HOMEDIR=`cd $HOMEDIR; pwd`
cd $CURDIR

export BLOCKCHAIN_CLASS_NAME="io.aegeus.AegeusBlockchain"

export IPFS_API_HOST="$IPFS_PORT_5001_TCP_ADDR"
export IPFS_API_PORT="$IPFS_PORT_5001_TCP_PORT"

export IPFS_GATEWAY_HOST="$IPFS_PORT_8080_TCP_ADDR"
export IPFS_GATEWAY_PORT="$IPFS_PORT_8080_TCP_PORT"

export AEG_JSONRPC_URL="$AEG_PORT_51473_TCP_ADDR:$AEG_PORT_51473_TCP_PORT"
export AEG_JSONRPC_USER="$AEG_ENV_RPCUSER"
export AEG_JSONRPC_PASS="$AEG_ENV_RPCPASS"

export RESTEASY_HOST=0.0.0.0
export RESTEASY_PORT=8081

JAVA_OPTS="-Xmx200m"

java $JAVA_OPTS -Dlog4j.configuration=file://$HOMEDIR/config/log4j.properties \
     -jar $HOMEDIR/lib/aegeus-jaxrs-1.0.0.Alpha4-SNAPSHOT.jar $@
