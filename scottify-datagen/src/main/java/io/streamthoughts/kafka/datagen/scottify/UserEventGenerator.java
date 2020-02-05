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
package io.streamthoughts.kafka.datagen.scottify;

import io.streamthoughts.kafka.datagen.scottify.domain.User;
import io.streamthoughts.kafka.datagen.scottify.domain.UserEvent;
import io.streamthoughts.kafka.datagen.scottify.domain.UserEventType;
import io.streamthoughts.kafka.datagen.scottify.internal.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static io.streamthoughts.kafka.datagen.scottify.internal.Utils.pickRandomElementFrom;

/**
 * Simple class for generating random user event.
 */

public class UserEventGenerator {

    private static Map<UserEventType, EventDataGenerator<String>> DATA_GENERATOR = new HashMap<>();

    static {

        DATA_GENERATOR.put(UserEventType.ALBUM_VIEW,
                (previous, database) -> Utils.pickRandomElementFrom(database.albums()).getKey());

        DATA_GENERATOR.put(UserEventType.ARTIST_VIEW,
                (previous, database) -> Utils.pickRandomElementFrom(database.artists()));

        DATA_GENERATOR.put(UserEventType.MUSIC_LISTEN_START,
                (previous, database) -> Utils.pickRandomElementFrom(database.musics()).getKey());

        DATA_GENERATOR.put(UserEventType.MUSIC_LISTEN_PAUSE,
                (previous, database) -> previous.getData().toString());

        DATA_GENERATOR.put(UserEventType.MUSIC_LISTEN_RESUME,
                (previous, database) -> previous.getData().toString());

        DATA_GENERATOR.put(UserEventType.MUSIC_LISTEN_STOP,
                (previous, database) -> previous.getData().toString());
    }

    private final Map<User, UserEvent<String>> lastGeneratedEventPerUser;

    private final Database database;

    /**
     * Creates a new {@link UserEventGenerator} instance.
     * @param database  the {@link Database} instance.
     */
    public UserEventGenerator(final Database database) {
        Objects.requireNonNull(database, "database can't be null");
        this.database = database;
        this.lastGeneratedEventPerUser = new ConcurrentHashMap<>(42);
    }

    public UserEvent<String> next() {
        return next(System.currentTimeMillis());
    }

    public UserEvent<String> next(long now) {
        User user = pickRandomElementFrom(database.users());

        UserEvent<String> event = lastGeneratedEventPerUser.get(user);

        final Integer transition = (event != null) ?
                pickRandomElementFrom(event.getType().validTransitions()) :
                pickRandomElementFrom(UserEventType.initialTransitions());

        UserEventType nextEventType = UserEventType.values()[transition];

        final String data = getEventDataGeneratorFor(nextEventType).generate(event, database);
        event = new UserEvent<>(user.getId(), nextEventType, now, data);

        if (!nextEventType.isTransient()) {
            lastGeneratedEventPerUser.put(user, event);
        }
        return event;
    }

    @SuppressWarnings("unchecked")
    private EventDataGenerator<String> getEventDataGeneratorFor(final UserEventType nextEventType) {
        EventDataGenerator<String> dataGenerator = DATA_GENERATOR.get(nextEventType);
        if (dataGenerator == null) {
            return EventDataGenerator.NO_OP;
        }
        return dataGenerator;
    }


    private interface EventDataGenerator<T> {

        EventDataGenerator NO_OP = (previous, database) -> null;

        T generate(final UserEvent<?> previous, final Database database);
    }
}
