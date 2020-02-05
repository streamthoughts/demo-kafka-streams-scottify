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
 * Music.
 */
public class Music implements WithKey, Serializable  {

    private final String artist;
    private final String title;
    private final String album;
    private final String duration;

    @JsonCreator
    public Music(@JsonProperty("title") final String title,
                 @JsonProperty("artist") final String artist,
                 @JsonProperty("album") final String album,
                 @JsonProperty("duration") final String duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music)) return false;
        Music music = (Music) o;
        return Objects.equals(title, music.title) &&
                Objects.equals(album, music.album) &&
                Objects.equals(duration, music.duration) &&
                Objects.equals(artist, music.artist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, album, duration, artist);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
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
        return title +";" + album;
    }

    public static class Builder {

        private String artist;
        private String title;
        private String album;
        private String duration;

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withArtist(final String artist) {
            this.artist =artist;
            return this;
        }

        public Builder withDuration(final String duration) {
            this.duration = duration;
            return this;
        }

        public Builder withAlbum(final String album) {
            this.album = album;
            return this;
        }

        public Music build() {
            return new Music(title, artist, album, duration);
        }

    }
}
