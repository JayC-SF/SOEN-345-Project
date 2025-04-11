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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedSetTest} for exercising the {@link TransformedSortedSet}
 * implementation.
 */
public class TransformedSortedSetTest<E> extends AbstractSortedSetTest<E> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedSet<E> makeFullCollection() {
        final SortedSet<E> set = new TreeSet<>(Arrays.asList(getFullElements()));
        return TransformedSortedSet.transformingSortedSet(set, (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedSet<E> makeObject() {
        return TransformedSortedSet.transformingSortedSet(new TreeSet<>(), (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTransformedSet() {
        final Transformer<E, E> transformer = (Transformer<E, E>) input -> {
            if (input == null) {
                throw new IllegalArgumentException("Null values are not allowed");
            }
            return input;
        };

        final SortedSet<E> originalSet = new TreeSet<>();
        final SortedSet<E> transformedSet = TransformedSortedSet.transformingSortedSet(originalSet, transformer);

        transformedSet.add((E) "Element1");
        assertTrue(transformedSet.contains((E) "Element1"));
        assertEquals(1, transformedSet.size());

        assertThrows(IllegalArgumentException.class, () -> transformedSet.add(null), "Adding null should throw an exception");
    }

    @Test
    public void testTransformedSet_decorateTransform() {
        final Transformer<String, String> transformer = input -> {
            if (input == null) {
                throw new IllegalArgumentException("Null values are not allowed");
            }
            return input.toUpperCase(); // Example transformation
        };
    
        final SortedSet<String> originalSet = new TreeSet<>();
        originalSet.add("a");
        originalSet.add("b");
    
        // Create a transformed set with consistent types
        final SortedSet<String> transformedSet = TransformedSortedSet.transformedSortedSet(originalSet, transformer);
    
        // Add transformed elements to the transformed set
        transformedSet.add("c");
        transformedSet.add("d");
    
        assertEquals(4, transformedSet.size());
        assertTrue(transformedSet.contains("A"));
        assertTrue(transformedSet.contains("B"));
        assertTrue(transformedSet.contains("C"));
        assertTrue(transformedSet.contains("D"));
    }

}