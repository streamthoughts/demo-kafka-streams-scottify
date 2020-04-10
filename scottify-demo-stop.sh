#!/usr/bin/env bash

set -e

PIDS=$(ps ax | grep -i 'scottify' | grep java | grep -v grep | awk '{print $1}')

if [ -z "$PIDS" ]; then
    echo "no scottify-topologies application to stop"
else
  echo -e "\nKilling scottify-topologies application $PIDS...\n"
  kill -s TERM $PIDS
fi

echo -e "\n🐳 Stopping all docker containers\n"
docker-compose down

echo -e "\n Removing all kafka-streams local states from : /tmp/kafka-streams/"
rm -rf /tmp/kafka-streams/scottify-topologies-*

exit 0