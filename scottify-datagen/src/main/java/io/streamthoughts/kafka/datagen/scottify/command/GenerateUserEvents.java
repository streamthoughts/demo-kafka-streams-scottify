/*
 * Copyright 2020 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.datagen.scottify.command;

import io.streamthoughts.kafka.datagen.scottify.Database;
import io.streamthoughts.kafka.datagen.scottify.UserEventGenerator;
import io.streamthoughts.kafka.datagen.scottify.domain.UserEvent;
import io.streamthoughts.kafka.datagen.scottify.internal.EventTime;
import io.streamthoughts.kafka.datagen.scottify.internal.Time;
import io.streamthoughts.kafka.datagen.scottify.kafka.KafkaProducerFactory;
import io.streamthoughts.kafka.datagen.scottify.serdes.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;
import static picocli.CommandLine.Mixin;

@Command(name = "events")
public class GenerateUserEvents implements Runnable {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
            .withZone(ZoneOffset.UTC);

    @Mixin
    DefaultOptions defaultOptions;

    @Mixin
    ProducerOptions producerOptions;

    @Option(names = "--max-messages",
            defaultValue = "-1",
            description = "the maximum number of message to generate")
    int messages;

    @Option(names = "--interval-ms",
            defaultValue = "0",
            description = "the time interval in milliseconds between two events")
    long intervalMs;

    @Option(names = "--from-datetime",
            description = "generate user events from datetime. \n" + "Format: 'YYYY-MM-DDTHH:mm:ss.SSS'")
    String fromDateTime;

    @Option(names = "--until-datetime",
            description = "generate user events until datetime. \n" + "Format: 'YYYY-MM-DDTHH:mm:ss.SSS'")
    String untilDataTime;

    @Option(names = "--advance-by",
            defaultValue = "0",
            description = "milliseconds")
    long advanceBy;

    private KafkaProducer<String, Object> producer;

    @Override
    public void run() {

        Database database = new Database();
        UserEventGenerator eventGenerator = new UserEventGenerator(database);
        mayInitializeKafkaProducer();

        if (messages == -1) {
            messages = Integer.MAX_VALUE;
        }

        Time time = fromDateTime != null ?
                new EventTime(getEpochTimeFromDate(fromDateTime).toEpochMilli()) :
                Time.SYSTEM;

        long untilEpoch = untilDataTime != null ?
                getEpochTimeFromDate(untilDataTime).toEpochMilli() :
                Long.MAX_VALUE;

        int produced = 0;
        long ts =  time.milliseconds();
        while (produced < messages && ts < untilEpoch) {

            UserEvent<String> event = eventGenerator.next(ts);

            if (defaultOptions.executionMode.print) {
                print(event);
            }

            if (defaultOptions.executionMode.generate) {
                producer.send(new ProducerRecord<>(
                    producerOptions.outputTopic,
                    event.getKey(),
                    event
                ));
            }
            time.advanceBy(advanceBy);
            time.sleep(intervalMs);
            ts =  time.milliseconds();
            produced++;
        }
    }

    private Instant getEpochTimeFromDate(String dt) {
        try {
            return Instant.from(FORMATTER.parse(dt));
        } catch (DateTimeParseException e) {
            throw new RuntimeException(
                "Error happens while parsing datetime \"" + dt + "\", expected format is \"YYYY-MM-DD'T'HH:mm:ss.SSS\"");
        }
    }

    private void mayInitializeKafkaProducer() {

        if (defaultOptions.executionMode.generate && producer == null) {
            KafkaProducerFactory factory = new KafkaProducerFactory();
            producer = factory.newProducer(
                    producerOptions.bootstrapServer,
                    new StringSerializer(),
                    new JsonSerializer());
        }
    }

    private void print(final Object object) {
        System.out.println(object);
    }

}
