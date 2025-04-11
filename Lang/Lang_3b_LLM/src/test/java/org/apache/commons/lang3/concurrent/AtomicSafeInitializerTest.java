package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code AtomicSafeInitializer}.
 *
 * @version $Id$
 */
public class AtomicSafeInitializerTest extends AbstractConcurrentInitializerTest {

    /** The instance to be tested. */
    private AtomicSafeInitializer<Object> initializer;

    /** A counter to verify initialize() is called exactly once. */
    private final AtomicInteger counter = new AtomicInteger(0);

    @Before
    public void setUp() throws Exception {
        initializer = new AtomicSafeInitializer<Object>() {
            @Override
            protected Object initialize() {
                counter.incrementAndGet();
                return new Object();
            }
        };
    }

    /**
     * Returns the initializer to be tested.
     *
     * @return the {@code AtomicSafeInitializer} under test
     */
    @Override
    protected ConcurrentInitializer<Object> createInitializer() {
        return initializer;
    }

    /**
     * Tests that the initializer actually returns a non-null value.
     */
    @Test
    public void testGetReturnsObject() throws Exception {
        Object obj = initializer.get();
        assertNotNull("Returned object should not be null", obj);
    }

    /**
     * Tests that initialize() is only called once even in a concurrent setting.
     */
    @Test
    public void testConcurrentInitializationOnlyOnce() throws Exception {
        final int numThreads = 10;
        final Object[] results = new Object[numThreads];
        final CountDownLatch latch = new CountDownLatch(numThreads);

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int index = i;
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        results[index] = initializer.get();
                    } catch (ConcurrentException e) {
                        fail("Exception during get(): " + e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                }
            });
            threads[i].start();
        }

        latch.await();

        Object first = results[0];
        for (int i = 1; i < numThreads; i++) {
            assertEquals("All threads should receive the same object", first, results[i]);
        }

        assertEquals("Initialize should be called only once", 1, counter.get());
    }
}
