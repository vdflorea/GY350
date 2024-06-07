sudo find . -type f -name "*.sh" -exec chmod +x {} \;

cd pipes
if [ ! -p "server.pipe" ]; then 
        mkfifo server.pipe
fi