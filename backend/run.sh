#!/bin/sh

cd /usr/src/

echo "Script started"

python3 import_script.py &> python.log

java -jar api.jar > java.log 2> error.log

#Docker stopper for debug
tail -f /etc/issue
