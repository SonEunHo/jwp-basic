#!/bin/bash
TOMCAT_WEBAPP_PATH = /home/deploy/apache-tomcat-8.5.35/webapps
TOMCAT_PATH = /home/deploy/apache-tomcat-8.5.35/bin
#WEBAPP_TARGET_PATH = /home/depl
cd ~/www/jwp-basic
git pull
mvn clean package
cd $TOMCAT_WEBAPP_PATH
rm -rf ROOT
cp -r /home/deploy/www/jwp-basic/target/jwp-basic ROOT
cd $TOMCAT_PATH
./shutdown.sh
./startup.sh