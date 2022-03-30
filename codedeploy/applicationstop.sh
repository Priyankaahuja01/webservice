#!/bin/bash
# sudo systemctl stop tomcat9
#sudo kill -9 `sudo lsof -t -i:8080`
# sudo rm -rf webapp/
# sudo rm -rf codedeploy/
# sudo rm -f appspec.yml
#sudo kill -9 `sudo lsof -t -i:8080`


# sudo ls -al
# sudo rm -rf target/
# sudo rm -rf codedeploy/
# sudo rm -f appspec.yml
sudo systemctl stop app.service
# sudo ls -al
echo "ending"
# sudo pwd
# sudo ls -al