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

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * CSVParserTest
 *
 * The test are organized in three different sections: The 'setter/getter' section, the lexer section and finally the
 * parser section. In case a test fails, you should follow a top-down approach for fixing a potential bug (its likely
 * that the parser itself fails if the lexer has problems...).
 */
public class CSVParserTest {

    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "name,age\nAlice,30\nBob,25";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals("Alice", records.get(0).get("name"));
        assertEquals("30", records.get(0).get("age"));
        assertEquals("Bob", records.get(1).get("name"));
        assertEquals("25", records.get(1).get("age"));

        parser.close();
    }

    @Test
    public void testEmptyInput() throws IOException {
        Reader in = new StringReader("");
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(0, records.size());

        parser.close();
    }

    @Test
    public void testNoHeaderAccessByIndex() throws IOException {
        String csvData = "a,b\nc,d";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        assertEquals("a", records.get(0).get(0));
        assertEquals("b", records.get(0).get(1));

        parser.close();
    }

    @Test
    public void testHeaderMap() throws IOException {
        String csvData = "name,age\nCharlie,40";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        Map<String, Integer> headerMap = parser.getHeaderMap();
        assertEquals((Integer) 0, headerMap.get("name"));
        assertEquals((Integer) 1, headerMap.get("age"));

        parser.close();
    }

    @Test
    public void testQuotedField() throws IOException {
        String csvData = "\"name\",\"note\"\n\"John\",\"He said, \"\"Hello\"\".\"";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = parser.getRecords();

        assertEquals("He said, \"Hello\".", records.get(0).get("note"));

        parser.close();
    }

    @Test
    public void testMultilineField() throws IOException {
        String csvData = "name,comment\n\"Jane\",\"Line1\nLine2\"";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = parser.getRecords();

        assertEquals("Line1\nLine2", records.get(0).get("comment"));

        parser.close();
    }

    @Test
    public void testIteratorAccess() throws IOException {
        String csvData = "name\nX\nY\nZ";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        Iterator<CSVRecord> it = parser.iterator();
        assertTrue(it.hasNext());
        assertEquals("X", it.next().get("name"));
        assertTrue(it.hasNext());
        assertEquals("Y", it.next().get("name"));
        assertTrue(it.hasNext());
        assertEquals("Z", it.next().get("name"));
        assertFalse(it.hasNext());

        parser.close();
    }

    @Test
    public void testRecordToMap() throws IOException {
        String csvData = "city,country\nParis,France";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        CSVRecord record = parser.getRecords().get(0);
        Map<String, String> map = record.toMap();

        assertEquals("France", map.get("country"));

        parser.close();
    }

    @Test
    public void testWhitespaceTrimming() throws IOException {
        String csvData = "name,age\n  Bob , 23 ";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreSurroundingSpaces());

        CSVRecord record = parser.getRecords().get(0);
        assertEquals("Bob", record.get("name"));
        assertEquals("23", record.get("age"));

        parser.close();
    }

    @Test
    public void testMultipleRecordsToMap() throws IOException {
        String csvData = "id,value\n1,one\n2,two\n3,three";
        Reader in = new StringReader(csvData);
        CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        List<CSVRecord> records = parser.getRecords();
        for (int i = 0; i < records.size(); i++) {
            CSVRecord rec = records.get(i);
            Map<String, String> map = rec.toMap();
            assertEquals(rec.get("value"), map.get("value"));
        }

        parser.close();
    }
}