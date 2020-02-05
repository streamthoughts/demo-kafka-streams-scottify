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

import io.streamthoughts.kafka.datagen.scottify.domain.Album;
import io.streamthoughts.kafka.datagen.scottify.domain.Music;
import io.streamthoughts.kafka.datagen.scottify.domain.User;
import io.streamthoughts.kafka.datagen.scottify.internal.ClasspathDelimitedFileLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Database. Utility class for loading all data.
 */
public class Database {

    private static final ClasspathDelimitedFileLoader Loader = new ClasspathDelimitedFileLoader();

    private final List<User> users;

    private final List<Music> musics;

    private final List<String> artists;

    private final List<Album> albums;

    /**
     * Creates a new {@link Database} instance.
     */
    public Database() {
        this.users = loadAllUsers();
        this.musics = loadAllMusics();
        this.albums = new ArrayList<>(loadAllAlbums());
        this.artists = this.albums
                        .stream()
                        .map(Album::getArtist).collect(Collectors.toList());
    }

    public List<User> users() {
        return users;
    }

    public List<Music> musics() {
        return musics;
    }

    public List<String> artists() {
        return artists;
    }

    public List<Album> albums() {
        return albums;
    }

    private static List<User> loadAllUsers() {

        return Loader.readAllLinesFrom("/dataset-users.csv", new AbstractLineCallback<>(";") {
            @Override
            public User apply(final String[] fields) {
                return new User(fields[0], fields[1], fields[3], fields[2]);
            }
        });
    }

    private static List<Music> loadAllMusics() {
        // artist;type;title;album;duration;release;
        return Loader.readAllLinesFrom("/dataset-musics.csv", new AbstractLineCallback<>(";") {
            @Override
            public Music apply(final String[] fields) {
                return Music.newBuilder()
                        .withAlbum(fields[3])
                        .withArtist(fields[0])
                        .withTitle(fields[2])
                        .withDuration(fields[4])
                        .build();
            }
        });
    }

    private static Set<Album> loadAllAlbums() {
        // artist;type;title;album;duration;release;
        return new HashSet<>(Loader.readAllLinesFrom("/dataset-musics.csv", new AbstractLineCallback<>(";") {
            @Override
            public Album apply(final String[] fields) {

                return Album.newBuilder()
                        .withArtist(fields[0])
                        .withType(fields[1])
                        .withName(fields[3])
                        .withRelease(Integer.parseInt(fields[5]))
                        .build();
            }
        }));
    }

    private static abstract class AbstractLineCallback<T> implements ClasspathDelimitedFileLoader.LineCallback<T> {

        private final String separator;

        AbstractLineCallback(final String separator) {
            Objects.requireNonNull(separator, "separator cannot be null");
            this.separator = separator;
        }

        @Override
        public T apply(final String line) {
            return  apply(line.split(separator));
        }

        public abstract T apply(final String[] fields);
    }
}
