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


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;


/**
 * UserEvent.
 */
public class UserEvent<T> implements WithKey, Serializable {

    private final String userId;
    private final UserEventType type;
    private final long timestamp;
    private final T data;

    /**
     * Creates a new {@link UserEvent} instance.
     *
     * @param userId        the user id.
     * @param type          the event type.
     * @param timestamp     the event timestamp.
     * @param data          the event data.
     */
    @JsonCreator
    public UserEvent(@JsonProperty("userId") final String userId,
                     @JsonProperty("type") final UserEventType type,
                     @JsonProperty("timestamp") final Long timestamp,
                     @JsonProperty("data") final T data) {
        Objects.requireNonNull(userId, "userId can't be null");
        Objects.requireNonNull(type, "type can't be null");
        Objects.requireNonNull(timestamp, "timestamp can't be null");
        this.userId = userId;
        this.type = type;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public UserEventType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    public boolean isOfType(final UserEventType type) {
        return getType().equals(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEvent)) return false;
        UserEvent userEvent = (UserEvent) o;
        return timestamp == userEvent.timestamp &&
                Objects.equals(userId, userEvent.userId) &&
                Objects.equals(type, userEvent.type) &&
                Objects.equals(data, userEvent.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, type, timestamp, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }

    @Override
    public String getKey() {
        return userId;
    }
}
