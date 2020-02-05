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
package io.streamthoughts.kafka.datagen.scottify.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Simple class for loading data from classpath.
 */
public class ClasspathDelimitedFileLoader {

    public <T> List<T> readAllLinesFrom(final String path, final LineCallback<T> callback) {

        final InputStream is = ClasspathDelimitedFileLoader.class.getResourceAsStream(path);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
          return br.lines().map(l -> {
              try {
                    return callback.apply(l);
              } catch (Exception e) {
                  System.out.println(l);
                  throw e;
              }
          }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error happens while loading classpath file : " + path, e);
        }
    }

    public interface LineCallback<T> extends Function<String, T> {
        @Override
        T apply(final String line);

    }
}
