#!/bin/bash

userID=$1
friendToAdd=$2

cd ../users


echo $friendToAdd >> "$userID/friendList.txt"