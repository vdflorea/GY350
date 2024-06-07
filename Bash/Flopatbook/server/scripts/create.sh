#!/bin/bash

userID=$1

cd ../users

# if [ -z "$userID" ]; then
#   echo "nok: no identifier provided"
#   exit 1
# fi
# if [ -d "$userID" ]; then
#     echo "nok: user already exists"
#     exit 1
# else
#     mkdir -p "$userID"
#     echo "ok - user $userID created"
# fi

mkdir -p "$userID"
touch "$userID"/friendList.txt "$userID"/wallFile.txt