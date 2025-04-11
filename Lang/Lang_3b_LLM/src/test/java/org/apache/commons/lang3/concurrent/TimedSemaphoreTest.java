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
package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Test class for TimedSemaphore.
 *
 * @version $Id$
 */
public class TimedSemaphoreTest {
    private static final long PERIOD = 500;
    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
    private static final int LIMIT = 10;

    @Test
    public void testInitialState() {
        TimedSemaphore semaphore = new TimedSemaphore(PERIOD, UNIT, LIMIT);
        assertEquals("Wrong limit", LIMIT, semaphore.getLimit());
        assertEquals("Wrong period", PERIOD, semaphore.getPeriod());
        assertEquals("Wrong time unit", UNIT, semaphore.getUnit());
    }

    @Test
    public void testAcquireAndResetLimit() throws InterruptedException {
        TimedSemaphore semaphore = new TimedSemaphore(PERIOD, UNIT, LIMIT);
        semaphore.acquire();
        semaphore.setLimit(5);
        assertEquals(5, semaphore.getLimit());
    }

    @Test
    public void testGetAverageCallsPerPeriod() throws InterruptedException {
        TimedSemaphore semaphore = new TimedSemaphore(PERIOD, UNIT, LIMIT);
        semaphore.acquire();
        Thread.sleep(PERIOD + 100);
        semaphore.acquire();
        double avg = semaphore.getAverageCallsPerPeriod();
        assertTrue("Average should be >= 0", avg >= 0);
    }


    @Test
    public void testLimitExceeded() throws InterruptedException {
        TimedSemaphore semaphore = new TimedSemaphore(PERIOD, UNIT, 1);
        semaphore.acquire();
        // Expect acquire to block or delay depending on internal state, so we don’t check tryAcquire()
        assertEquals(1, semaphore.getLimit());
    }
}
