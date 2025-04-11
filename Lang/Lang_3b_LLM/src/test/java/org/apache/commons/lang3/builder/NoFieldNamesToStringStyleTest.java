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
package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ToStringStyle.NO_FIELD_NAMES_STYLE}.
 */
public class NoFieldNamesToStringStyleTest {

    private final Integer base = Integer.valueOf(5);
    private final String baseStr = base.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(base));

    @Before
    public void setUp() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.NO_FIELD_NAMES_STYLE);
    }

    @After
    public void tearDown() throws Exception {
        ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
    }

    private static class Person {
        private String name;
        private int age;
        private String profession;

        public Person(String name, int age, String profession) {
            this.name = name;
            this.age = age;
            this.profession = profession;
        }
    }

    private static class CollectionHolder {
        ArrayList<String> list = new ArrayList<String>();
    }

    private static class MapHolder {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
    }

    @Test
    public void testBasicObjectOutput() {
        Person person = new Person("Jane", 28, "Architect");
        String toString = ToStringBuilder.reflectionToString(person);
    
        assertTrue("Should contain class name", toString.startsWith(person.getClass().getName()));
        assertTrue("Should contain value", toString.contains("Jane"));
    }
    
    @Test
    public void testArrayFormatting() {
        int[] numbers = {1, 2, 3};
        String result = ToStringBuilder.reflectionToString(numbers);
    
        assertTrue("Should contain array brackets", result.contains("[") && result.contains("]"));
    }
    

    @Test
    public void testNullHandling() {
        Person person = new Person(null, 0, null);
        String result = ToStringBuilder.reflectionToString(person);

        assertTrue("Should show null fields", result.contains("<null>"));
    }

    @Test
    public void testCollectionFormatting() {
        CollectionHolder holder = new CollectionHolder();
        holder.list.add("hello");
        holder.list.add("world");

        String result = ToStringBuilder.reflectionToString(holder);
        assertTrue("Should include elements", result.contains("hello") && result.contains("world"));
    }

    @Test
    public void testMapFormatting() {
        MapHolder holder = new MapHolder();
        holder.map.put("one", 1);
        holder.map.put("two", 2);

        String result = ToStringBuilder.reflectionToString(holder);
        assertTrue("Should contain keys", result.contains("one") && result.contains("two"));
    }
}
