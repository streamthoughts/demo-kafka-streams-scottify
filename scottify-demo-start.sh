#!/usr/bin/env bash

set -e

APP_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout)

usage="USAGE: $0 [--build (true|false)] --num-instances [1-9]+"

BUILD="true"
NUM_INSTANCE=1
while [ $# -gt 0 ]; do
  COMMAND=$1
  case $COMMAND in
    --build)
      shift
      BUILD=$1
      shift
      ;;
    --num-instances)
      shift
      NUM_INSTANCE=$1
      shift
      ;;
    --help)
      echo $usage
      exit 1
      ;;
    *)
      break
      ;;
  esac
done

echo "--------------------------------------------------------------------------------"
echo "---                 Demo : Azkarra Streams - Scotiffy                        ---"
echo "--------------------------------------------------------------------------------"

echo -e "\n🏭 Building Maven Project (mvn clean package -q -DskipTests)\n"

if [[ "$BUILD" == "true" ]]; then
  mvn clean package -q -DskipTests
fi

echo -e "\n🐳 Starting single-node Kafka cluster\n"
docker-compose up -d

KAFKA_CONTAINER_NAME=azkarra-cp-broker

echo -e "\n⏳ Waiting for Kafka Broker to be up and running\n"
while true
do
  if [ $(docker logs $KAFKA_CONTAINER_NAME 2>&1 | grep "started (kafka.server.KafkaServer)" >/dev/null; echo $?) -eq 0 ]; then
    echo
    break
  fi
  printf "."
  sleep 1
done;

echo -e "\n⏳ Creating all Kafka topics\n"

function createTopic() {
    docker exec -it $KAFKA_CONTAINER_NAME kafka-topics --create \
  --topic $1 --partitions $2 --replication-factor $3 \
  --zookeeper cp-zookeeper:2181 --if-not-exists
}

DEFAULT_REPICATION_FACTOR=1

createTopic db-users 1 $DEFAULT_REPICATION_FACTOR
createTopic db-albums 1 $DEFAULT_REPICATION_FACTOR
createTopic db-musics 1 $DEFAULT_REPICATION_FACTOR
createTopic events-user-activity 6 $DEFAULT_REPICATION_FACTOR


export PATH="$PATH:$(pwd)/scottify-datagen/target/scottify-datagen-$APP_VERSION-dist/scottify-datagen/bin/"

echo -e "\n⏳ Generating data for 'Albums' into topic db-albums"
scottify-datagen albums --bootstrap-servers localhost:9092 --output-topic db-albums --generate

echo -e "\n⏳ Generating data for 'Users' into topic db-users"
scottify-datagen users --bootstrap-servers localhost:9092 --output-topic db-users --generate

echo -e "\n⏳ Generating data for 'Events' into topic events-user-activity (interval-ms: 100 max-messages 10000)"
scottify-datagen events --generate --bootstrap-servers localhost:9092 \
--output-topic events-user-activity \
--max-messages 10000

# Remove all logs files
rm -rf ./logs/azkarra-console-*.log && mkdir -p ./logs

i=1
while [[ $i -le $NUM_INSTANCE ]]; do
   PORT="808$i"
   echo -e "\n🚀 Starting Azkarra application instance : http://localhost:$PORT/ui"
   STATE_DIR=/tmp/kafka-streams/scottify-topologies-$i
   rm -rf $STATE_DIR && mkdir -p $STATE_DIR
   nohup java -jar scottify-topologies/target/scottify-topologies-"$APP_VERSION".jar \
	   --azkarra.server.port $PORT \
	   --azkarra.context.streams.state.dir $STATE_DIR > ./logs/azkarra-console-$PORT.log 2>&1 < /dev/null &
   ((i = i + 1))
done