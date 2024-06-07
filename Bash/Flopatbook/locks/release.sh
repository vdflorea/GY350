#! /bin/bash

if [ -z "$1" ]; then
		echo "Usage ./acquire.sh <lock-name>" >&1
		exit 1
else
		if [ -f "$1" ]; then 
			rm "$1"
		fi
		exit 0
fi