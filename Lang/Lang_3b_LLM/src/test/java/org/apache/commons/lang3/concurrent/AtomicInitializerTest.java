package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.concurrent.ConcurrentInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.junit.Test;

/**
 * Test class for {@code AtomicInitializer}.
 *
 * @version $Id$
 */
public class AtomicInitializerTest extends AbstractConcurrentInitializerTest {

    /**
     * Returns the initializer to be tested.
     *
     * @return the {@code AtomicInitializer}
     */
    @Override
    protected ConcurrentInitializer<Object> createInitializer() {
        return new AtomicInitializer<Object>() {
            @Override
            protected Object initialize() throws ConcurrentException {
                return new Object();
            }
        };
    }

    /**
     * Verifies that multiple threads can safely use the initializer concurrently.
     */
    @Test
    public void testConcurrentInitialization() throws Exception {
        final AtomicInitializer<Object> initializer = new AtomicInitializer<Object>() {
            @Override
            protected Object initialize() throws ConcurrentException {
                return new Object();
            }
        };

        final int numThreads = 10;
        final Object[] results = new Object[numThreads];
        final CountDownLatch latch = new CountDownLatch(numThreads);

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int idx = i;
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        results[idx] = initializer.get();
                    } catch (ConcurrentException e) {
                        fail("Exception in thread: " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
            threads[i].start();
        }

        latch.await();

        Object reference = results[0];
        for (int i = 1; i < numThreads; i++) {
            assertNotNull("Result should not be null", results[i]);
            if (results[i] != reference) {
                fail("Initializer did not return same object across threads");
            }
        }
    }
}
