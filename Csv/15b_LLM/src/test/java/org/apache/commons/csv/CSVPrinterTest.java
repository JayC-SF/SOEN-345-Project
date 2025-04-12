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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;
import static org.junit.Assert.*; //added this line

import org.apache.commons.io.FileUtils;
import org.h2.value.Value;
import org.h2.value.ValueArray;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class CSVPrinterTest {

    @Test
    public void testPrintRecordSimple() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("Alice", 30, true);
        printer.flush();

        assertTrue(sw.toString().contains("Alice"));
        assertTrue(sw.toString().contains("30"));
        assertTrue(sw.toString().contains("true"));
    }

    @Test
    public void testPrintMultipleRecords() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("Header1", "Header2");
        printer.printRecord("Row1Col1", "Row1Col2");
        printer.printRecord("Row2Col1", "Row2Col2");
        printer.flush();

        String output = sw.toString();
        assertTrue(output.contains("Row1Col1"));
        assertTrue(output.contains("Row2Col2"));
    }

    @Test
    public void testQuoteHandling() throws IOException {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        CSVPrinter printer = new CSVPrinter(sw, format);
        printer.printRecord("value1", "value,with,commas");
        printer.flush();

        assertTrue(sw.toString().contains("\"value,with,commas\""));
    }

    @Test
    public void testCustomDelimiter() throws IOException {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        CSVPrinter printer = new CSVPrinter(sw, format);
        printer.printRecord("A", "B");
        printer.flush();

        assertTrue(sw.toString().contains("A;B"));
    }

    @Test
    public void testNullValues() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("a", null, "c");
        printer.flush();

        assertTrue(sw.toString().contains("a,,c"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormatShouldThrow() throws IOException {
        new CSVPrinter(new StringWriter(), null);
    }

    @Test
    public void testPrintIterable() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        List<String> values = Arrays.asList("X", "Y", "Z");
        printer.printRecord(values);
        printer.flush();

        assertTrue(sw.toString().contains("X,Y,Z"));
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        CSVPrinter printer = new CSVPrinter(sw, format);
        printer.printComment("This is a comment");
        printer.flush();

        assertTrue(sw.toString().startsWith("# This is a comment"));
    }

    @Test
    public void testFlushAndClose() throws IOException {
        StringWriter mockWriter = mock(StringWriter.class);
        CSVPrinter printer = new CSVPrinter(mockWriter, CSVFormat.DEFAULT);
        printer.flush();
        printer.close();

        verify(mockWriter, times(1)).flush();
        verify(mockWriter, times(1)).close();
    }

    @Test
    public void testPrintNullRecord() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.print(null);
        printer.flush();

        assertEquals("", sw.toString().trim());
    }
}
