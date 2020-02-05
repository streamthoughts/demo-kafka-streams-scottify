/*
 * Copyright 2019 StreamThoughts.
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
package io.streamthoughts.scottify.demo.model;


import io.streamthoughts.kafka.datagen.scottify.domain.UserEvent;
import io.streamthoughts.kafka.datagen.scottify.domain.UserEventType;

import java.util.Objects;

public class SongListenedEvent {

    public final String title;
    public final String album;

    /**
     * Static helper method to create a {@link SongListenedEvent} from the given event.
     * @param event the event to read.
     *
     * @return  a new {@link SongListenedEvent} instance.
     */
    public static SongListenedEvent parseEventPayload(final UserEvent event) {
        if (event.getType() != UserEventType.MUSIC_LISTEN_START)
            throw new IllegalArgumentException("Invalid event type: " + event.getType());

        String[] albumTrack = ((String)event.getData()).split(";");
        return new SongListenedEvent(albumTrack[0], albumTrack[1]);
    }

    /**
     * Creates a new {@link SongListenedEvent} instance.
     *
     * @param title the title of the song.
     * @param album the album of the song.
     */
    public SongListenedEvent(final String title, final String album) {
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.album = Objects.requireNonNull(album, "albums cannot be null");
    }
}