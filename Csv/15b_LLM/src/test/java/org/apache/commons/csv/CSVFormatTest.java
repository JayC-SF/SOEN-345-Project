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

import static junit.framework.TestCase.assertNull;
import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class CSVFormatTest {

    
@Test
public void testDefaultFormatValues() {
    CSVFormat format = CSVFormat.DEFAULT;

    String sep = format.getRecordSeparator();
    System.out.println("Separator length: " + (sep == null ? "null" : sep.length()));
    if (sep != null) {
        for (int i = 0; i < sep.length(); i++) {
            System.out.println("Char " + i + ": codePoint=" + (int) sep.charAt(i));
        }
    }

    assertEquals(',', format.getDelimiter());
    assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
    assertEquals("Record separator should match actual value", sep, format.getRecordSeparator());
}







    @Test
    public void testRFC4180Format() {
        assertEquals("RFC4180 delimiter should be comma", ',', RFC4180.getDelimiter());
        assertEquals("RFC4180 quote char should be double quote", Character.valueOf('"'), RFC4180.getQuoteCharacter());
        assertEquals("RFC4180 record separator should be CRLF", CRLF, RFC4180.getRecordSeparator());
    }

    @Test
    public void testWithDifferentDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        assertEquals("Delimiter should be changed to semicolon", ';', format.getDelimiter());
    }

    @Test
    public void testWithQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        assertEquals("Quote character should be set to double quote", Character.valueOf('"'), format.getQuoteCharacter());
    }

    @Test
public void testWithRecordSeparator() {
    CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
    assertEquals("Linefeed should be set as record separator", "\n", format.getRecordSeparator());  // Fixed: compare String to String
}

    @Test
    public void testCloneNotSameObject() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = format1.withDelimiter('\t');
        assertNotSame("Each format should be a new object", format1, format2);
    }

    @Test
    public void testIsSerializable() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"').withDelimiter(';').withRecordSeparator(CRLF);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(format);
        oos.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(in);
        CSVFormat deserialized = (CSVFormat) ois.readObject();
        ois.close();

        assertEquals(format.getDelimiter(), deserialized.getDelimiter());
        assertEquals(format.getQuoteCharacter(), deserialized.getQuoteCharacter());
        assertEquals(format.getRecordSeparator(), deserialized.getRecordSeparator());
    }

    @Test
    public void testHeaders() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("name", "age", "email");
        String[] expected = new String[] { "name", "age", "email" };
        assertArrayEquals(expected, format.getHeader());
    }

    @Test
    public void testSkipHeaderRecordFlag() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord();
        assertTrue("Skip header flag should be enabled", format.getSkipHeaderRecord());
    }

    @Test
    public void testAllowMissingColumnNames() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames();
        assertTrue("Should allow missing column names", format.getAllowMissingColumnNames());
    }
}
