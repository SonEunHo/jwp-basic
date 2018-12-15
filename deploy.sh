#!/bin/bash
TOMCAT_WEBAPP_PATH=/home/deploy/apache-tomcat-8.5.35/webapps
TOMCAT_PATH=/home/deploy/apache-tomcat-8.5.35/bin
WEBAPP_PATH=/home/deploy/www/jwp-basic
WEBAPP_RELEASE_PATH=/home/deploy/www/jwp-basic/release

cd $WEBAPP_PATH
pwd
git pull
mvn clean package
C_TIME=$(date +%s)
echo $C_TIME
mv target/jwp-basic $WEBAPP_RELEASE_PATH/$C_TIME
cd $TOMCAT_WEBAPP_PATH
rm -rf ROOT
ln -s $WEBAPP_PATH/$C_TIME ROOT
cd $TOMCAT_PATH
bash shutdown.sh
bash startup.sh