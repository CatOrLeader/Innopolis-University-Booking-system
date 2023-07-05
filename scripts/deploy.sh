#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/swagger-spring-1.0.0.jar \
    root@185.91.53.106:/home/root

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa root@185.91.53.106 << EOF

pgrep java | killall -9 java
nohup java -jar /home/root/swagger-spring-1.0.0.jar -b > log.txt 2>1&
sudo rm -rf  /etc/nginx/sites-enabled/default.save
exit

EOF

echo 'Bye'