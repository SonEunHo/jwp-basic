WEPAPP_PATH=/home/deploy/www/jwp-basic
WEBAPP_RELEASE_PATH=/home/deploy/www/jwp-basic/release
TOMCAT_WEBAPP_PATH=/home/deploy/apache-tomcat-8.5.35/webapps
TOMCAT_PATH=/home/deploy/apache-tomcat-8.5.35/bin

cd $WEBAPP_RELEASE_PATH
find . -regex ".*/[0-9.]+" -printf "%T@ %f\n" | sort | cut -d' ' -f2
ls -Art | tail -n 1

RELEASES=$(ls -1t)

echo "releases : $RELEASES"

REVISIONS=(${RELEASES//\n/})


if [${#REVISIONS[@]} -lt 2]; then
  echo "releases source length more than 2"
else
  echo "roll back to directory : ${REVISIONS[1]}"
  cd $TOMCAT_WEBAPP_PATH
  rm -rf ROOT
  ln -s $WEBAPP_RELEASE_PATH/${REVISIONS[1]} ROOT
  cd $TOMCAT_PATH
  bash shutdown.sh
  bash startup.sh
  cd $WEBAPP_RELEASE_PATH
  rm -rf ${REVISIONS[0]}
fi