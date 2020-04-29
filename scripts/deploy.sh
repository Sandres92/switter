#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/switter-london.pem \
    target/switter-1.0-SNAPSHOT.jar \
    ec2-user@ec2-35-179-11-222.eu-west-2.compute.amazonaws.com:/home/ec2-user/

echo 'Restart server...'

ssh -i ~/.ssh/switter-london.pem ec2-user@ec2-35-179-11-222.eu-west-2.compute.amazonaws.com << EOF

pgrep java | xargs kill -9
nohup java -jar switter-1.0-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'
