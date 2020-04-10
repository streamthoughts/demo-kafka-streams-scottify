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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility methods.
 */
public class Utils {

    public static  <T> T pickRandomElementFrom(final List<T> l) {
        return l.get(getRandom(l.size()));
    }

    public static int getRandom(int max) {
        return ThreadLocalRandom.current().nextInt(0, max);
    }

}