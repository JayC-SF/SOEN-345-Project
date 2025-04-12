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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

public class AssertionsTest {

    @Test
    public void testBasicCSVParsing() throws Exception {
        String csvData = "Name,Age\nAlice,30\nBob,25";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());
        assertEquals("Alice", records.get(0).get("Name"));
        assertEquals("25", records.get(1).get("Age"));
        parser.close();
    }

    @Test
    public void testQuotedFieldsWithCommas() throws Exception {
        String csvData = "Name,Quote\n\"Doe, John\",\"Hello, world!\"";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withFirstRecordAsHeader());
        CSVRecord record = parser.getRecords().get(0);

        assertEquals("Doe, John", record.get("Name"));
        assertEquals("Hello, world!", record.get("Quote"));
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
public void testMissingFields() throws Exception {
    String csvData = "Name,Age\nAlice,30\nBob";
    CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withFirstRecordAsHeader());
    List<CSVRecord> records = parser.getRecords();

    // Accessing a missing value should throw an exception
    records.get(1).get("Age");
    parser.close();
}


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHeaderAccess() throws Exception {
        String csvData = "Name,Age\nAlice,30";
        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withFirstRecordAsHeader());
        CSVRecord record = parser.getRecords().get(0);

        // This should throw an exception
        record.get("Nonexistent");
        parser.close();
    }
}
