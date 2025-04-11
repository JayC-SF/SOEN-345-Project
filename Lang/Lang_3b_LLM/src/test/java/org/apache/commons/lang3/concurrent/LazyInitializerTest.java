package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test class for {@code LazyInitializer}.
 */
public class LazyInitializerTest extends AbstractConcurrentInitializerTest {
    /** The initializer to be tested. */
    private LazyInitializerTestImpl initializer;

    @Before
    public void setUp() {
        initializer = new LazyInitializerTestImpl();
    }

    @Override
    protected ConcurrentInitializer<Object> createInitializer() {
        return initializer;
    }

    @Test
    public void testSameInstanceReturned() throws ConcurrentException {
        Object obj1 = initializer.get();
        Object obj2 = initializer.get();
        assertSame("Should return same instance", obj1, obj2);
    }

    @Test
    public void testLazyInitialization() {
        assertFalse("Object should not be initialized before get()", initializer.isInitialized());
    }

    @Test
    public void testThreadSafeInitialization() throws Exception {
        final int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);
        final Object[] results = new Object[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(new Runnable() {
                public void run() {
                    try {
                        results[index] = initializer.get();
                    } catch (ConcurrentException e) {
                        results[index] = e;
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        latch.await();
        executor.shutdown();

        Object expected = results[0];
        for (int i = 1; i < threadCount; i++) {
            assertSame("All threads should receive the same instance", expected, results[i]);
        }
    }

    private static class LazyInitializerTestImpl extends LazyInitializer<Object> {
        private boolean initialized = false;

        @Override
        protected Object initialize() {
            initialized = true;
            return new Object();
        }

        public boolean isInitialized() {
            return initialized;
        }
    }
}
