#!/bin/bash

while true; do

    cd ../locks
    # Release after sending output to $id.pipe
    ./release.sh "mainLock.txt"
        
    cd ../pipes
    read input < server.pipe
    set -- $input

    # echo $input

    request=$1
    id=$2

    cd ../locks
    # Acquire before entering critical section
    ./acquire.sh "mainLock.txt"

    cd ../server/scripts

    case $request in
        create)
            if [ $# -gt 2 ]; then 
                cd ../../pipes 
                echo "ERROR: permission denied-- user $id cannot create new users" > $id.pipe
                continue
            fi

            cd ../users

            if [ ! -d "$id" ]; then

                cd ../scripts
                ./create.sh $id

                cd ../../pipes
                echo "SUCCESS: user $id created" > $id.pipe
            else
                cd ../../pipes
                echo "ERROR: user $id already exists" > $id.pipe
            fi
            ;;
        add)
            if [ ! $# -eq 3 ]; then

                cd ../../pipes 
                echo "ERROR: invalid number of arguments" > $id.pipe
                continue
            fi

            friendToAdd=$3

            cd ../users

            if [ "$id" = "$friendToAdd" ]; then
                cd ../../pipes
                echo "ERROR: cannot add yourself" > $id.pipe
                continue
            fi

            if [ ! -d "$friendToAdd" ]; then

                cd ../../pipes
                echo "ERROR: user $friendToAdd does not exist" > $id.pipe
                continue
            fi

            if grep "$friendToAdd" "$id/friendList.txt" > /dev/null; then

                cd ../../pipes
                echo "ERROR: user $friendToAdd already in $id friend list" > $id.pipe
                continue
            fi

            cd ../scripts
            ./add_friend.sh $id $friendToAdd

            cd ../../pipes
            echo "SUCCESS: user $friendToAdd added to $id friend list" > $id.pipe
            ;;
        post)
            if [ $# -lt 4 ]; then

                cd ../../pipes
                echo "ERROR: invalid number of arguments" > $id.pipe
                continue
    
            fi

            receiver=$3
            message=$4

            cd ../users

            if [ ! -d "$receiver" ]; then 

                cd ../../pipes
                echo "ERROR: user $receiver does not exist" > $id.pipe
                continue

            fi

            if ! grep -qF "$id" "$receiver/friendList.txt" && [ $id != $receiver ]; then # if sender not on receiver friend list and if not displaying own wall
                cd ../../pipes
                echo "ERROR: user $id not on $receiver friend list" > $id.pipe
                continue
            fi


            if [ -n "$5" ]; then
                for arg in "${@:5}"; do
                    message+=" $arg"
                done
            fi

            cd ../scripts
            ./post_messages.sh $id $receiver "$message"

            cd ../../pipes
            echo "SUCCESS: message added to $receiver wall" > $id.pipe
            ;;
        display)
            if [ $# -ne 3 ]; then
                cd ../../pipes
                echo "ERROR: invalid number of arguments" > $id.pipe
                continue
            fi

            cd ../users

            userID=$3

            if [ ! -d "$userID" ]; then
                cd ../../pipes
                echo "ERROR: user $userID does not exist" > $id.pipe
                continue
            fi

            output=""

            output+=`cat $userID/wallFile.txt`

            cd ../../pipes
            echo "$output" > $id.pipe
            ;;
        *)
            cd ../../pipes
            echo "ERROR: invalid request!" > $id.pipe
            ;;
    esac
done