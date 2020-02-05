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


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.streamthoughts.kafka.datagen.scottify.domain.Album;
import io.streamthoughts.kafka.datagen.scottify.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserListenedSongsByGenre {

    private String userName;
    private Map<String, Long> listenedPerGenre;

    public UserListenedSongsByGenre() {
        this.listenedPerGenre = new HashMap<>();
    }

    @JsonCreator
    public UserListenedSongsByGenre(@JsonProperty("userName") final String userName,
                                    @JsonProperty("listenedPerGenre") final Map<String, Long> listenedPerGenre) {
        this.userName = userName;
        this.listenedPerGenre = listenedPerGenre;
    }

    public UserListenedSongsByGenre update(final User user, final Album album) {
        this.userName = user.getName();
        this.listenedPerGenre.putIfAbsent(album.getType(), 0L);
        this.listenedPerGenre.compute(album.getType(), (key, counter) -> counter + 1L);
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Map<String, Long> getListenedPerGenre() {
        return listenedPerGenre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserListenedSongsByGenre)) return false;
        UserListenedSongsByGenre that = (UserListenedSongsByGenre) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(listenedPerGenre, that.listenedPerGenre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, listenedPerGenre);
    }
}
