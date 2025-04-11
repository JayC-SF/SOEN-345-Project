/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the {@link TransformedSet}
 * implementation.
 */
public class TransformedSetTest<E> extends AbstractSetTest<E> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public Set<E> makeConfirmedCollection() {
        return new HashSet<>();
    }

    @Override
    public Set<E> makeConfirmedFullCollection() {
        return new HashSet<>(Arrays.asList(getFullElements()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<E> makeFullCollection() {
        final Set<E> list = new HashSet<>(Arrays.asList(getFullElements()));
        return TransformedSet.transformingSet(list,
                (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<E> makeObject() {
        return TransformedSet.transformingSet(new HashSet<>(),
                (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
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

        final Set<E> originalSet = new HashSet<>();
        final Set<E> transformedSet = TransformedSet.transformingSet(originalSet, transformer);

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

    final Set<String> originalSet = new HashSet<>();
    originalSet.add("a");
    originalSet.add("b");

    // Create a transformed set with consistent types
    final Set<String> transformedSet = TransformedSet.transformedSet(originalSet, transformer);

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