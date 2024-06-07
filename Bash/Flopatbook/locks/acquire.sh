#! /bin/bash

lockName=$1

if [ -z "$lockName" ]; then
		echo "Usage ./acquire.sh <lock-name>" >&1
		exit 1
else
		# the 'link' (ln) system call is supposed to be atomic on a local file system
		while ! ln -s "$0" "$lockName" 2>/dev/null; do
				sleep 1
		done

		exit 0
fi

