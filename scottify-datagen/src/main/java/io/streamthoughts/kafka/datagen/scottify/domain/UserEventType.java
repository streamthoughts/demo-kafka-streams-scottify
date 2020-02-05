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
package io.streamthoughts.kafka.datagen.scottify.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UserEventType.
 *
 */
public enum UserEventType {

    USER_LOGIN(false, 2, 3, 4, 5),

    USER_LOGIN_ATTEMPT(true),

    USER_LOGOUT(false, 0, 1),

    ARTIST_VIEW(true),

    ALBUM_VIEW(true),

    MUSIC_LISTEN_START(false, 2, 3, 4, 5, 6, 7),

    MUSIC_LISTEN_STOP(false, 2, 3, 4, 5),

    MUSIC_LISTEN_PAUSE(false, 2, 3, 4, 5, 8),

    MUSIC_LISTEN_RESUME(false, 2, 3, 4, 5, 6, 7);

    private final boolean isTransient;

    private final List<Integer> validTransitions = new ArrayList<>();

    /**
     * Creates a new {@link UserEventType} instance.
     *
     * @param isTransient       is this event is transient.
     * @param validTransitions  the valid transitions after this events. If empty then all transitions are valid.
     */
    UserEventType(final boolean isTransient, final Integer... validTransitions) {
        this.isTransient = isTransient;
        this.validTransitions.addAll(Arrays.asList(validTransitions));
    }

    public boolean isTransient() {
        return isTransient;
    }

    public List<Integer> validTransitions() {
        return validTransitions;
    }

    public static List<Integer> initialTransitions() {
        return Arrays.asList(0, 1);
    }
}
