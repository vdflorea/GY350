#!/bin/bash

# ---------- How to Use ----------
# 1) Run ./client.sh <user>
# 2) Create new user if <user> does not exist
# 3) User can submit following commands:
#   --> request --> "add"   arguments --> <user-to-add>
#   --> request --> "post"  arguments --> <receiver> <message-without-quotes>
#   --> request --> "display"   arguments --> <user>


cd ../pipes

id=$1

trap '
 echo -e "\n"

 if [ -d /pipes ] && [ ! -d ../users ]; then # in case there is a user called pipes
   rm pipes/$id.pipe
 elif [ -d "../pipes" ]; then
   rm ../pipes/$id.pipe
 elif [ -d "../../pipes" ]; then
   rm ../../pipes/$id.pipe
 fi

 echo "CTRL + C executed. Removing $id.pipe. Exiting..."
 exit
' SIGINT 


if [ "$#" -eq 1 ]; then
    echo "Username given: $id"

    if [ ! -p "server.pipe" ]; then 
        mkfifo server.pipe
    fi

    cd ../server/users

    if [ ! -d "$id" ]; then

        echo "ERROR: User not found"
        read -p "Would you like to create a new user? --> $id (yes/no) : " choice
        
        case "$choice" in
            yes)

                cd ../../pipes

                echo "create $id" > server.pipe

                cd ../server/users
                ;;
            no)
                echo "Bye!" && exit
                ;;
            *)
                echo "Invalid input. Use (yes/no) next time. Bye!" && exit 
                ;;
        esac
    fi

    echo -e "User $id logged in!\n"

    cd ../../pipes

    if [ ! -p "$id.pipe" ]; then 
        mkfifo $id.pipe
    fi

    while true; do 
        
        # This will create a file with output in /pipes
        if timeout 1s cat $id.pipe > serverOutput; then
            printf "%s\n" "$(cat serverOutput)"
        fi

        # Concatenate spaces to cause invalid request to occur in server.sh
        # --> eg. "create bob" for request --> "createbob" = invalid request
        read -a words -p "Enter request: "
        request="${words[*]}"
        request="${request// /}"

        # Ensure request will always be $1 and id $2 in server.sh
        if [ -z "$request" ]; then 
            request="invalid"
        fi

        read -p "Enter arguments: " args

        echo $request $id $args > server.pipe
    done
else
    echo "Invalid parameters given"
fi

