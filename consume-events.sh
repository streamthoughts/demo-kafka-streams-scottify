#!/usr/bin/env bash

docker exec -it -e KAFKA_OPTS="" broker \
kafka-console-consumer \
--topic event-user-activity \
--from-beginning \
--bootstrap-server broker:29092
