#!/bin/bash

sender=$1
receiver=$2
message=$3

cd ../users

echo "$sender: $message" >> $receiver/wallFile.txt