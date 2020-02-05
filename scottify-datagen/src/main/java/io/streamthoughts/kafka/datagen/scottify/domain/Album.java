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
 * Album.
 */
public class Album implements WithKey, Serializable {

    private final String name;
    private final String artist;
    private final int release;
    private final String type;

    /**
     * Creates a new {@link Album} instance.
     * @param artist    the album artist.
     * @param name      the album name.
     * @param release   the album release year.
     * @param type      the music type.
     */
    @JsonCreator
    public Album(@JsonProperty("artist") final String artist,
                 @JsonProperty("name") final String name,
                 @JsonProperty("release") final int release,
                 @JsonProperty("type") final String type) {
        this.artist = artist;
        this.name = name;
        this.release = release;
        this.type = type;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public int getRelease() {
        return release;
    }

    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return release == album.release &&
                Objects.equals(name, album.name) &&
                Objects.equals(type, album.type) &&
                Objects.equals(artist, album.artist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, release, type, artist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", release=" + release +
                ", type='" + type + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return name;
    }

    public static class Builder {

        private String name;
        private String artist;
        private int release;
        private String type;

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withArtist(final String artist) {
            this.artist =artist;
            return this;
        }

        public Builder withRelease(final int release) {
            this.release = release;
            return this;
        }

        public Builder withType(final String type) {
            this.type = type;
            return this;
        }

        public Album build() {
            return new Album(artist, name, release, type);
        }

    }
}
