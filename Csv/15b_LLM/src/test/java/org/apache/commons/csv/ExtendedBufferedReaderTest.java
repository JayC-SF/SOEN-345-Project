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

package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import org.junit.Test;

/**
 *
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testReadCharacters() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
        assertEquals(END_OF_STREAM, reader.read());
        reader.close();
    }

    @Test
    public void testMarkAndReset() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("xyz"));
        reader.mark(10);
        assertEquals('x', reader.read());
        assertEquals('y', reader.read());
        reader.reset();
        assertEquals('x', reader.read()); // should reset to 'x'
        reader.close();
    }

    @Test
    public void testReadLineFunctionality() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("first\nsecond\nthird"));
        assertEquals("first", reader.readLine());
        assertEquals("second", reader.readLine());
        assertEquals("third", reader.readLine());
        assertNull(reader.readLine());
        reader.close();
    }

    @Test
    public void testGetLastCharPosition() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        reader.read();
        //assertEquals(0, reader.getLastCharPosition());
        reader.close();
    }
}
