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

import org.junit.Assert;
import org.junit.Test;

public class CSVFormatPredefinedTest {

    @Test
    public void testPredefinedFormatsNotNull() {
        for (CSVFormat.Predefined predefined : CSVFormat.Predefined.values()) {
            CSVFormat format = predefined.getFormat();
            Assert.assertNotNull("Format for " + predefined.name() + " should not be null", format);
        }
    }

    @Test
    public void testExcelFormat() {
        CSVFormat excel = CSVFormat.Predefined.Excel.getFormat();
        Assert.assertEquals("Excel format should use comma as delimiter", ',', excel.getDelimiter());
        Assert.assertTrue("Excel format should allow quotes", excel.getQuoteCharacter() != null);
    }

    @Test
    public void testRFC4180Format() {
        CSVFormat rfc = CSVFormat.Predefined.RFC4180.getFormat();
        Assert.assertEquals("RFC4180 format should use comma as delimiter", ',', rfc.getDelimiter());
        Assert.assertTrue("RFC4180 format should allow quotes", rfc.getQuoteCharacter() != null);
        Assert.assertTrue("RFC4180 should use CRLF as record separator", "\r\n".equals(rfc.getRecordSeparator()));
    }

    @Test
    public void testTDFFormat() {
        CSVFormat tdf = CSVFormat.Predefined.TDF.getFormat();
        Assert.assertEquals("TDF format should use tab as delimiter", '\t', tdf.getDelimiter());
    }

    @Test
    public void testMySQLFormat() {
        CSVFormat mysql = CSVFormat.Predefined.MySQL.getFormat();
        Assert.assertEquals("MySQL format should use tab as delimiter", '\t', mysql.getDelimiter());
        Assert.assertEquals("MySQL format should use \\n as record separator", "\n", mysql.getRecordSeparator());
    }
}

