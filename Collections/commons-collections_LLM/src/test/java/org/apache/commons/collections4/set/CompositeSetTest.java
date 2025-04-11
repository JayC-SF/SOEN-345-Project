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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link CompositeSet} implementation.
 */
public class CompositeSetTest<E> extends AbstractSetTest<E> {

    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildTwo() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddComposited() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildTwo();

        compositeSet.addComposited(setOne, setTwo);

        assertTrue(compositeSet.contains((E) "1"));
        assertTrue(compositeSet.contains((E) "3"));
        assertEquals(4, compositeSet.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddCompositedCollision() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildOne(); // Intentional collision

        assertThrows(IllegalArgumentException.class, () -> compositeSet.addComposited(setOne, setTwo));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testContains() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();

        compositeSet.addComposited(setOne);

        assertTrue(compositeSet.contains((E) "1"));
        assertFalse(compositeSet.contains((E) "3"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testContainsAll() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildTwo();

        compositeSet.addComposited(setOne, setTwo);

        assertTrue(compositeSet.containsAll(setOne));
        assertTrue(compositeSet.containsAll(setTwo));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFailedCollisionResolution() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildOne(); // Intentional collision
    
        compositeSet.setMutator(new SetMutator<E>() {
            @Override
            public void resolveCollision(CompositeSet<E> composite, Set<E> existing, Set<E> added, Collection<E> intersects) {
                throw new UnsupportedOperationException("Collision resolution not supported");
            }
    
            @Override
            public boolean add(CompositeSet<E> composite, List<Set<E>> sets, E obj) {
                throw new UnsupportedOperationException("Add operation not supported");
            }
    
            @Override
            public boolean addAll(CompositeSet<E> composite, List<Set<E>> sets, Collection<? extends E> coll) {
                throw new UnsupportedOperationException("AddAll operation not supported");
            }
        });
    
        assertThrows(UnsupportedOperationException.class, () -> compositeSet.addComposited(setOne, setTwo));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveAll() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildTwo();

        compositeSet.addComposited(setOne, setTwo);
        compositeSet.removeAll(setOne);

        assertFalse(compositeSet.contains((E) "1"));
        assertTrue(compositeSet.contains((E) "3"));
        assertEquals(2, compositeSet.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveComposited() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();
        Set<E> setTwo = buildTwo();

        compositeSet.addComposited(setOne, setTwo);
        compositeSet.removeComposited(setOne);

        assertFalse(compositeSet.contains((E) "1"));
        assertTrue(compositeSet.contains((E) "3"));
        assertEquals(2, compositeSet.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemoveUnderlying() {
        CompositeSet<E> compositeSet = makeObject();
        Set<E> setOne = buildOne();

        compositeSet.addComposited(setOne);
        setOne.remove((E) "1");

        assertFalse(compositeSet.contains((E) "1"));
        assertEquals(1, compositeSet.size());
    }

}