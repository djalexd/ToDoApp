#!/bin/bash
set +e

# Script responsible for stopping the Docker container that is currently
# running and starting a new version.

APP_NAME=${1}
VERSION=${2}
if [ "${APP_NAME}" == "" ]; then
 echo 'Usage: ./stop-start-runtime-v1.sh {app_name} {version}'
 exit 1
fi

if [ "${VERSION}" == "" ]; then
  echo 'Usage ./stop-start-runtime-v1.sh {app_name} {version}'
  exit 1
fi

ID=`(docker ps -f name=${APP_NAME} --format='{{.ID}}')`
if [ -n "${ID}" ]; then
  echo "Attempting to stop container with ID=${ID}"
  docker stop ${ID}
  docker rm ${ID}
else
  echo "No container needs to be stopped"
fi

ID=`(docker run -d -P --memory '300m' --name ${APP_NAME} ${APP_NAME}:${VERSION})`
echo "Running container ${APP_NAME}:${VERSION} with id=${ID}"