#!/usr/bin/env bash

set -e

PIDS=$(ps ax | grep -i 'scottify' | grep java | grep -v grep | awk '{print $1}')

if [ -z "$PIDS" ]; then
    echo "no scottify-topologies application to stop"
else
  echo "killing scottify-topologies application $PIDS..."
  kill -s TERM $PIDS
fi

docker-compose down

echo "removing all kafka-streams local states from : /tmp/kafka-streams/"
rm -rf /tmp/kafka-streams/scottify-topologies-*

exit 0