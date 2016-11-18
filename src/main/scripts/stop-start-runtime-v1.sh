#!/bin/bash
set +e

# Script responsible for stopping the Docker container that is currently
# running and starting a new version.

APP_NAME=${1}
if [ "${APP_NAME}" == "" ]; then
 echo 'Usage: ./stop-start-runtime-v1.sh {app_name}'
 exit 1
fi

ID=`(docker ps -f ancestor=${APP_NAME}:latest --format='{{.ID}}')`
if [ -n "${ID}" ]; then
  echo "Attempting to stop container with ID=${ID}"
  docker stop ${ID}
else
  echo "No container needs to be stopped"
fi

ID=`(docker run -d -P ${APP_NAME}:latest)`
echo "Running container ${APP_NAME}:latest with id=${ID}"