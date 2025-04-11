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
package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests {@link org.apache.commons.lang3.builder.HashCodeBuilder} and
 * {@link org.apache.commons.lang3.builder.EqualsBuilder} to ensure that equal
 * objects must have equal hash codes.
 * 
 * @version $Id$
 */
public class HashCodeBuilderAndEqualsBuilderTest {

    @Test
    public void testIntegerWithTransients() {
        testInteger(true);
    }

    @Test
    public void testIntegerWithoutTransients() {
        testInteger(false);
    }

    private void testInteger(final boolean testTransients) {
        final Integer i1 = Integer.valueOf(12345);
        final Integer i2 = Integer.valueOf(12345);
        assertEqualsAndHashCodeContract(i1, i2, testTransients);
    }

    private void assertEqualsAndHashCodeContract(Object a, Object b, boolean testTransients) {
        // Step 1: equals() must return true
        assertTrue("Objects not equal", a.equals(b));
        assertTrue("Equality is not symmetric", b.equals(a));

        // Step 2: hashCode() must return same value
        assertEquals("Hash codes must match", a.hashCode(), b.hashCode());
    }
}
