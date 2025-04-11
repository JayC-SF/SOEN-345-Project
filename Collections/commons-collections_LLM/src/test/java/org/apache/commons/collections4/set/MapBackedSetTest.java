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
package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.jupiter.api.Test;

/**
 * JUnit test.
 */
public class MapBackedSetTest<E> extends AbstractSetTest<E> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Set<E> makeObject() {
        return MapBackedSet.mapBackedSet(new HashedMap<>());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAdd() {
        Set<E> set = makeObject();
        assertTrue(set.add((E) "Element1"));
        assertTrue(set.contains((E) "Element1"));
        assertEquals(1, set.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemove() {
        Set<E> set = makeObject();
        set.add((E) "Element1");
        assertTrue(set.remove((E) "Element1"));
        assertFalse(set.contains((E) "Element1"));
        assertEquals(0, set.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testClear() {
        Set<E> set = makeObject();
        set.add((E) "Element1");
        set.add((E) "Element2");
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testContains() {
        Set<E> set = makeObject();
        set.add((E) "Element1");
        assertTrue(set.contains((E) "Element1"));
        assertFalse(set.contains((E) "Element2"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSize() {
        Set<E> set = makeObject();
        assertEquals(0, set.size());
        set.add((E) "Element1");
        assertEquals(1, set.size());
        set.add((E) "Element2");
        assertEquals(2, set.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIsEmpty() {
        Set<E> set = makeObject();
        assertTrue(set.isEmpty());
        set.add((E) "Element1");
        assertFalse(set.isEmpty());
    }
}