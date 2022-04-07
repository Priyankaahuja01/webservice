#!/bin/bash

echo "in after install"
# sudo pwd
# sudo ls -al

#sudo kill -9 `sudo lsof -t -i:8080`
pid=$(sudo lsof -i tcp:8080 -t)
echo $pid


sudo systemctl stop app.service

#removing previous build ROOT folder

# sudo rm -rf /home/ec2-user/*.jar

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/cloudwatch_agent_config.json -s
sudo /bin/systemctl status amazon-cloudwatch-agent
sudo /bin/systemctl stop amazon-cloudwatch-agent
sudo /bin/systemctl start amazon-cloudwatch-agent