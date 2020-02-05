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
package io.streamthoughts.kafka.datagen.scottify.command;

import io.streamthoughts.kafka.datagen.scottify.Database;
import io.streamthoughts.kafka.datagen.scottify.domain.WithKey;

import java.util.Collection;

import static picocli.CommandLine.Command;

@Command( name = "musics")
public class GenerateMusics extends DefaultGenerate {

    @Override
    protected Collection<? extends WithKey> get(final Database db) {
        return db.musics();
    }
}
