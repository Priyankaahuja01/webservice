#!/bin/bash
echo "before install"
#sudo pwd
#sudo ls -al
#sudo systemctl stop tomcat9

sudo systemctl stop app.service

rm -rf /home/ec2-user/*.jar
