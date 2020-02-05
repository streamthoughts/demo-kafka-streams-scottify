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
package io.streamthoughts.kafka.datagen.scottify.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaProducerFactory {

    public <K, V> KafkaProducer<K, V> newProducer(final String bootstrapServers,
                                           final Serializer<K> keySerialiser,
                                           final Serializer<V> valueSerializer) {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return newProducer(producerConfig, keySerialiser, valueSerializer);
    }

    public <K, V> KafkaProducer<K, V> newProducer(final Map<String, Object> producerConfigs,
                                           final Serializer<K> keySerialiser,
                                           final Serializer<V> valueSerializer) {
        return new KafkaProducer<>(producerConfigs, keySerialiser, valueSerializer);
    }
}
