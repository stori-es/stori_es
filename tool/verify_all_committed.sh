#!/bin/bash

RESULT=`git status --porcelain`

if [ x"$RESULT" != x"" ]; then
    echo "uncommitted changes found" 1>&2
    exit 1
fi
