#!/bin/bash

MAIN_PATH=`dirname $0`/..
cd $MAIN_PATH
echo `git branch | grep '*' | cut -d' ' -f2` > .targetBranch
