#!/bin/bash

set -u

SCRIPT_DIR="$ (cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo $SCRIPT_DIR

BASE_DIR=/home/bhavesh3184/deployment

PACKAGE_TAR="$1"
STAR_STOP="$2"

if [$# -eq 3] ; then
DEPLOYER_ID="$3"
BASE_DIR="$BASE_DIR"/"$DEPLOYER_ID"
fi

CORRECT_NAME=$( echo "$PACKAGE_TAR" | grep -Eo "^[a-zA-Z]*(.*)\.tar.gz$" )
    if ["$CORRECT_NAME$ == @@ ] ; then
    echo "Incorrect Package Name"
    echo "Package Name is ": $PACKAGE_TAR"
    exit 1;
fi


export MODULE_NAME=$( echo "$PACKAGE_TAR" | sed 's/^\([a-zA-Z-]*\)-[0-9].*/\1/g' )
export VERSION=$( echo "$PACKAGE_TAR" | sed 's/[a-zA-Z-]*\([0-9].*\).tar.gz$/\1/g' )

export MODULE_DIR=$( echo $BASE_DIR/$MODULE_NAME | sed -e 's#spark-streaming-kafka-consumer#realtime#g' )
