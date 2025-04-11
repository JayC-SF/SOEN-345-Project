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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link ListOrderedSet} implementation.
 */
public class ListOrderedSetTest<E>
    extends AbstractSetTest<E> {

    static class A {

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof A || obj instanceof B;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    static class B {

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof A || obj instanceof B;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static final Integer ZERO = Integer.valueOf(0);

    private static final Integer ONE = Integer.valueOf(1);

    private static final Integer TWO = Integer.valueOf(2);

    private static final Integer THREE = Integer.valueOf(3);

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public ListOrderedSet<E> makeObject() {
        return ListOrderedSet.listOrderedSet(new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    protected ListOrderedSet<E> setupSet() {
        final ListOrderedSet<E> set = makeObject();

        for (int i = 0; i < 10; i++) {
            set.add((E) Integer.toString(i));
        }
        return set;
    }

    @Test
    public void testDecorator() {
        final Set<E> set = new HashSet<>();
        final ListOrderedSet<E> listOrderedSet = ListOrderedSet.listOrderedSet(set);
        assertSame(set, listOrderedSet.decorated()); // Use decorated() instead of getSet()
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDuplicates() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) new A());
        set.add((E) new B());
        assertEquals(1, set.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListAddIndexed() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add(1, (E) TWO); // Add at index 1
        assertEquals(3, set.size());
        assertEquals(TWO, set.get(1));
        assertEquals(ONE, set.get(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListAddRemove() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add((E) TWO);
        assertEquals(3, set.size());

        set.remove((E) ONE);
        assertEquals(2, set.size());
        assertTrue(set.contains((E) ZERO));
        assertTrue(set.contains((E) TWO));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListAddReplacing() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add((E) TWO);
    
        // Simulate replacing an element by removing and adding
        set.remove((E) ONE);
        set.add((E) THREE);
    
        assertEquals(3, set.size());
        assertTrue(set.contains((E) ZERO));
        assertTrue(set.contains((E) TWO));
        assertTrue(set.contains((E) THREE));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOrdering() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add((E) TWO);

        final Iterator<E> iterator = set.iterator();
        assertEquals(ZERO, iterator.next());
        assertEquals(ONE, iterator.next());
        assertEquals(TWO, iterator.next());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRetainAll() {
        final ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add((E) TWO);

        final Collection<E> retain = new ArrayList<>();
        retain.add((E) ZERO);
        retain.add((E) TWO);

        set.retainAll(retain);
        assertEquals(2, set.size());
        assertTrue(set.contains((E) ZERO));
        assertTrue(set.contains((E) TWO));
    }
}