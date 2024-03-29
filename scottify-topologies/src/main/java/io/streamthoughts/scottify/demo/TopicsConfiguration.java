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
package io.streamthoughts.scottify.demo;

import io.streamthoughts.azkarra.api.annotations.Component;
import io.streamthoughts.azkarra.api.annotations.Factory;
import io.streamthoughts.azkarra.api.annotations.Restricted;
import io.streamthoughts.azkarra.api.components.Restriction;
import org.apache.kafka.clients.admin.NewTopic;

/**
 * The {@code TopicsConfiguration} is used to automatically create Kafka Topics for the
 * {@link AggregateUsersListenedSongsByGenreTopology} topology.
 */
@Factory
public class TopicsConfiguration {

    @Component
    @Restricted(type = Restriction.TYPE_STREAMS, names = "CountUsersListenMusicPerGenreTopology")
    public NewTopic topicAlbums() {
        return new NewTopic("db-albums", 1, (short) 1);
    }

    @Component
    @Restricted(type = Restriction.TYPE_STREAMS, names = "CountUsersListenMusicPerGenreTopology")
    public NewTopic topicUsers() {
        return new NewTopic("db-users", 1, (short) 1);
    }

    @Component
    @Restricted(type = Restriction.TYPE_STREAMS, names = "CountUsersListenMusicPerGenreTopology")
    public NewTopic topicEvents() {
        return new NewTopic("events-user-activity", 6, (short) 1);
    }

}
