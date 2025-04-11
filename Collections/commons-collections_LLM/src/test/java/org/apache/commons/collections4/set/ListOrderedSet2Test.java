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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the {@link ListOrderedSet}
 * implementation.
 */
public class ListOrderedSet2Test<E> extends AbstractSetTest<E> {

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
        return new ListOrderedSet<>();
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
    @SuppressWarnings("unchecked")
    public void testListAddIndexed() {
        ListOrderedSet<E> set = makeObject();
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
        ListOrderedSet<E> set = makeObject();
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
    public void testOrdering() {
        ListOrderedSet<E> set = makeObject();
        set.add((E) ZERO);
        set.add((E) ONE);
        set.add((E) TWO);

        Iterator<E> iterator = set.iterator();
        assertEquals(ZERO, iterator.next());
        assertEquals(ONE, iterator.next());
        assertEquals(TWO, iterator.next());
    }
}