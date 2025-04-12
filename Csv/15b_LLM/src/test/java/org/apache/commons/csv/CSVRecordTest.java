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
package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

    private CSVRecord record;
    private Map<String, Integer> headerMap;

    @Before
    public void setUp() {
        headerMap = new HashMap<>();
        headerMap.put("Name", 0);
        headerMap.put("Age", 1);
        headerMap.put("City", 2);

        String[] values = {"Alice", "30", "Montreal"};
        record = new CSVRecord(values, headerMap, "source.csv", 1L, 0L);

    }

    @Test
    public void testGetByIndex() {
        assertEquals("Alice", record.get(0));
        assertEquals("30", record.get(1));
        assertEquals("Montreal", record.get(2));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByInvalidIndex() {
        record.get(5);
    }

    @Test
    public void testGetByName() {
        assertEquals("Alice", record.get("Name"));
        assertEquals("30", record.get("Age"));
        assertEquals("Montreal", record.get("City"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByInvalidName() {
        record.get("InvalidColumn");
    }

    @Test
    public void testIsMapped() {
        assertTrue(record.isMapped("Name"));
        assertFalse(record.isMapped("InvalidColumn"));
    }

    @Test
    public void testIsSet() {
        assertTrue(record.isSet("Name"));
        assertFalse(record.isSet("InvalidColumn"));
    }

    @Test
    public void testToMap() {
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Name", "Alice");
        expectedMap.put("Age", "30");
        expectedMap.put("City", "Montreal");

        assertEquals(expectedMap, record.toMap());
    }

    @Test
    public void testSize() {
        assertEquals(3, record.size());
    }

    @Test
    public void testIterator() {
        Iterator<String> iterator = record.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Alice", iterator.next());
        assertEquals("30", iterator.next());
        assertEquals("Montreal", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
public void testGetCharacterPosition() {
    String[] values = {"value1", "value2"};
    Map<String, Integer> headerMap = new HashMap<>();
    headerMap.put("col1", 0);
    headerMap.put("col2", 1);
    CSVRecord record = new CSVRecord(values, headerMap, "source.csv", 1L, 1L); // Set character position to 1L
    assertEquals(1L, record.getCharacterPosition());
}


    @Test
    public void testGetRecordNumber() {
        assertEquals(1L, record.getRecordNumber());
    }

    @Test
    public void testToString() {
        assertNotNull(record.toString());
        assertTrue(record.toString().contains("Alice"));
    }
}
