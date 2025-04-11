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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link PredicatedSet} implementation.
 */
public class PredicatedSetTest<E> extends AbstractSetTest<E> {

    protected Predicate<E> truePredicate = TruePredicate.<E>truePredicate();

    protected Predicate<E> testPredicate =
        String.class::isInstance;

    protected PredicatedSet<E> decorateSet(final Set<E> set, final Predicate<? super E> predicate) {
        return PredicatedSet.predicatedSet(set, predicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] getFullElements() {
        return (E[]) new Object[] {"1", "3", "5", "7", "2", "4", "6"};
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public PredicatedSet<E> makeObject() {
        return decorateSet(new HashSet<>(), truePredicate);
    }

    protected PredicatedSet<E> makeTestSet() {
        return decorateSet(new HashSet<>(), testPredicate);
    }

    @Test
    public void testGetSet() {
        PredicatedSet<E> set = makeTestSet();
        assertNotNull(set.decorated(), "Decorated set should not be null");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIllegalAdd() {
        PredicatedSet<E> set = makeTestSet();
        assertThrows(IllegalArgumentException.class, () -> set.add((E) Integer.valueOf(10)), 
            "Adding an element that does not match the predicate should throw an exception");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIllegalAddAll() {
        PredicatedSet<E> set = makeTestSet();
        Set<E> elements = new HashSet<>();
        elements.add((E) "ValidString");
        elements.add((E) Integer.valueOf(10)); // Invalid element
        assertThrows(IllegalArgumentException.class, () -> set.addAll(elements), 
            "Adding elements that do not match the predicate should throw an exception");
    }
}