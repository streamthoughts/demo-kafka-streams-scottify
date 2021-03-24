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
 * User.
 */
public class User implements WithKey, Serializable {

    private final String id;
    private final String name;
    private final String gender;
    private final String species;

    /**
     * Creates a new {@link User} instance.
     *
     * @param name      the user name.
     * @param gender    the user gender.
     * @param species   the user species.
     */
    @JsonCreator
    public User(@JsonProperty("id") final String id,
                @JsonProperty("name") final String name,
                @JsonProperty("gender")final String gender,
                @JsonProperty("species")final String species) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.species = species;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getSpecies() {
        return species;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", species='" + species + '\'' +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return name;
    }
}
