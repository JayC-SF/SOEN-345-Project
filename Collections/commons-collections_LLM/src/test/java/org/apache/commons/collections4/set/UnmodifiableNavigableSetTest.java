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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractNavigableSetTest} for exercising the
 * {@link UnmodifiableNavigableSet} implementation.
 */
public class UnmodifiableNavigableSetTest<E> extends AbstractNavigableSetTest<E> {
    protected UnmodifiableNavigableSet<E> set;
    protected ArrayList<E> array;

    @Override
    public String getCompatibilityVersion() {
        return "4.1";
    }

    @Override
    public boolean isAddSupported() {
        return false;
    }

    @Override
    public boolean isRemoveSupported() {
        return false;
    }

    @Override
    public UnmodifiableNavigableSet<E> makeFullCollection() {
        final TreeSet<E> set = new TreeSet<>(Arrays.asList(getFullElements()));
        return (UnmodifiableNavigableSet<E>) UnmodifiableNavigableSet.unmodifiableNavigableSet(set);
    }

    @Override
    public NavigableSet<E> makeObject() {
        return UnmodifiableNavigableSet.unmodifiableNavigableSet(new TreeSet<>());
    }

    @SuppressWarnings("unchecked")
    protected void setupSet() {
        set = makeFullCollection();
        array = new ArrayList<>();
        array.add((E) Integer.valueOf(1));
    }

    @Test
    public void testComparator() {
        setupSet();
        Comparator<? super E> comparator = set.comparator();
        assertNull(comparator, "Comparator should be null for natural ordering");
    }

    @Test
    public void testDecorateFactory() {
        final TreeSet<E> treeSet = new TreeSet<>();
        final NavigableSet<E> unmodifiableSet = UnmodifiableNavigableSet.unmodifiableNavigableSet(treeSet);
        assertSame(treeSet, ((UnmodifiableNavigableSet<E>) unmodifiableSet).decorated(), 
            "The decorated set should be the same as the original set");
    }

    /**
     * Verify that base set and subsets are not modifiable
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testUnmodifiable() {
        setupSet();
        verifyUnmodifiable(set);
        verifyUnmodifiable(set.descendingSet());
        verifyUnmodifiable(set.headSet((E) Integer.valueOf(1)));
        verifyUnmodifiable(set.headSet((E) Integer.valueOf(1), true));
        verifyUnmodifiable(set.tailSet((E) Integer.valueOf(1)));
        verifyUnmodifiable(set.tailSet((E) Integer.valueOf(1), false));
        verifyUnmodifiable(set.subSet((E) Integer.valueOf(1), (E) Integer.valueOf(3)));
        verifyUnmodifiable(set.subSet((E) Integer.valueOf(1), false, (E) Integer.valueOf(3), false));
        verifyUnmodifiable(set.subSet((E) Integer.valueOf(1), true, (E) Integer.valueOf(3), true));
    }

    /**
     * Verifies that a set is not modifiable
     */
    @SuppressWarnings("unchecked")
    public void verifyUnmodifiable(final Set<E> set) {
        assertThrows(UnsupportedOperationException.class, () -> set.add((E) Integer.valueOf(2)), "Add operation should throw UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, () -> set.remove((E) Integer.valueOf(1)), "Remove operation should throw UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, set::clear, "Clear operation should throw UnsupportedOperationException");
    }
}