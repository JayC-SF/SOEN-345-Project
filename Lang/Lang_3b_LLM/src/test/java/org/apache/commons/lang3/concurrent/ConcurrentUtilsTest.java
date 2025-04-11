/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Test class for {@link ConcurrentUtils}.
 *
 * @version $Id$
 */
public class ConcurrentUtilsTest {

    @Test
    public void testConstantFutureGet() throws ExecutionException, InterruptedException {
        Future<Object> future = ConcurrentUtils.constantFuture((Object) "test");
        assertEquals("test", future.get());
    }

    @Test
    public void testCreateIfAbsentNew() throws Exception {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
        String key = "key";
        String value = ConcurrentUtils.createIfAbsent(map, key, new ConcurrentInitializer<String>() {
            public String get() throws ConcurrentException {
                return "value";
            }
        });
        assertEquals("value", value);
    }

    @Test
    public void testCreateIfAbsentExisting() throws Exception {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("key", "oldValue");
        String value = ConcurrentUtils.createIfAbsent(map, "key", new ConcurrentInitializer<String>() {
            public String get() throws ConcurrentException {
                return "newValue";
            }
        });
        assertEquals("oldValue", value);
    }

    @Test
    public void testHandleCauseUnchecked() throws Exception {
        ExecutionException t = new ExecutionException("failure", new IllegalArgumentException("bad arg"));
        try {
            ConcurrentUtils.handleCause(t);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testHandleCauseChecked() throws Exception {
        ExecutionException t = new ExecutionException("failure", new Exception("checked"));
        try {
            ConcurrentUtils.handleCause(t);
            fail("Should throw ConcurrentException");
        } catch (ConcurrentException expected) {
            assertEquals("checked", expected.getCause().getMessage());
        }
    }

    @Test
    public void testHandleCauseError() throws Exception {
        ExecutionException t = new ExecutionException("failure", new AssertionError("error"));
        try {
            ConcurrentUtils.handleCause(t);
            fail("Should throw AssertionError");
        } catch (AssertionError expected) {
            // expected
        }
    }
}