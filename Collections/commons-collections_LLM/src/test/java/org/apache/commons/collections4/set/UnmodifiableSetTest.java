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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.Unmodifiable;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for exercising the
 * {@link UnmodifiableSet} implementation.
 */
public class UnmodifiableSetTest<E> extends AbstractSetTest<E> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
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
    public Set<E> makeFullCollection() {
        final HashSet<E> set = new HashSet<>(Arrays.asList(getFullElements()));
        return UnmodifiableSet.unmodifiableSet(set);
    }

    @Override
    public Set<E> makeObject() {
        return UnmodifiableSet.unmodifiableSet(new HashSet<>());
    }

    @Test
    public void testDecorateFactory() {
        final HashSet<E> set = new HashSet<>();
        final UnmodifiableSet<E> unmodifiableSet = (UnmodifiableSet<E>) UnmodifiableSet.unmodifiableSet(set);
        assertSame(set, unmodifiableSet.decorated(), "The decorated set should be the same as the original set");
        assertTrue(unmodifiableSet instanceof Unmodifiable, "The set should implement the Unmodifiable interface");
    }

    @Test
    public void testUnmodifiable() {
        final Set<E> set = makeFullCollection();
        assertThrows(UnsupportedOperationException.class, () -> set.add((E) "NewElement"), "Add operation should throw UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, () -> set.remove((E) "ExistingElement"), "Remove operation should throw UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, set::clear, "Clear operation should throw UnsupportedOperationException");
    }

}