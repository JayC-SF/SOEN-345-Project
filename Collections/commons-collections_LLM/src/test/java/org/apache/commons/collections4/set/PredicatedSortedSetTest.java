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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedSetTest} for exercising the
 * {@link PredicatedSortedSet} implementation.
 */
public class PredicatedSortedSetTest<E> extends AbstractSortedSetTest<E> {

    protected Predicate<E> truePredicate = TruePredicate.<E>truePredicate();

    protected Predicate<E> testPredicate =
        o -> o instanceof String && ((String) o).startsWith("A");

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedSet<E> makeFullCollection() {
        final TreeSet<E> set = new TreeSet<>(Arrays.asList(getFullElements()));
        return PredicatedSortedSet.predicatedSortedSet(set, truePredicate);
    }

    @Override
    public SortedSet<E> makeObject() {
        return PredicatedSortedSet.predicatedSortedSet(new TreeSet<>(), truePredicate);
    }

    protected PredicatedSortedSet<E> makeTestSet() {
        return PredicatedSortedSet.predicatedSortedSet(new TreeSet<>(), testPredicate);
    }

    @Test
    public void testComparator() {
        SortedSet<E> set = makeObject();
        Comparator<? super E> comparator = set.comparator();
        assertNull(comparator, "Comparator should be null for natural ordering");
    }

    @Test
    public void testGetSet() {
        PredicatedSortedSet<E> set = makeTestSet();
        assertNotNull(set.decorated(), "Decorated set should not be null");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIllegalAdd() {
        PredicatedSortedSet<E> set = makeTestSet();
        assertThrows(IllegalArgumentException.class, () -> set.add((E) "B"), 
            "Adding an element that does not match the predicate should throw an exception");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIllegalAddAll() {
        PredicatedSortedSet<E> set = makeTestSet();
        Set<E> elements = new TreeSet<>(Arrays.asList((E) "A1", (E) "B1"));
        assertThrows(IllegalArgumentException.class, () -> set.addAll(elements), 
            "Adding elements that do not match the predicate should throw an exception");
    }
}