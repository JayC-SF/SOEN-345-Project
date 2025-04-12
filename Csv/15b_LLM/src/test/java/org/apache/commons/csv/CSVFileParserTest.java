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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CSVFileParserTest {

    private final File csvFile;

    public CSVFileParserTest(File file) {
        this.csvFile = file;
    }

    @Parameters(name = "{0}")
    public static Collection<File> csvFiles() {
        List<File> files = new ArrayList<File>();
        try {
            URL dirURL = CSVFileParserTest.class.getClassLoader().getResource("csv_test_files");
            if (dirURL != null) {
                File dir = new File(dirURL.getFile());
                File[] csvFiles = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".csv");
                    }
                });
                if (csvFiles != null) {
                    files.addAll(Arrays.asList(csvFiles));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    @Test
    public void testCSVFileParsing() {
        assertNotNull("CSV file should not be null", csvFile);
        assertTrue("CSV file should exist: " + csvFile.getAbsolutePath(), csvFile.exists());

        BufferedReader reader = null;
        CSVParser parser = null;
        try {
            reader = new BufferedReader(new FileReader(csvFile));
            parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            List<CSVRecord> records = parser.getRecords();

            assertTrue("CSV should have at least one record", records.size() > 0);
            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                assertEquals("All records must have correct number of values",
                        parser.getHeaderMap().size(), record.size());
            }

        } catch (IOException e) {
            fail("IOException while parsing file: " + e.getMessage());
        } finally {
            try {
                if (parser != null) parser.close();
                if (reader != null) reader.close();
            } catch (IOException ignored) {}
        }
    }
}
