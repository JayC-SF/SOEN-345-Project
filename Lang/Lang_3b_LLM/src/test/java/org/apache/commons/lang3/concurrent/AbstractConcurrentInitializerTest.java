package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

public abstract class AbstractConcurrentInitializerTest {

    protected ConcurrentInitializer<Object> createInitializer() {
        return new AtomicInitializer<Object>() {
            @Override
            protected Object initialize() {
                return new Object(); // or return any specific test object
            }
        };
    }

    @Test
    public void testGetCreatesNonNullObject() throws Exception {
        ConcurrentInitializer<Object> initializer = createInitializer();
        Object obj = initializer.get();
        assertNotNull("Object must not be null", obj);
    }

    @Test
    public void testGetReturnsSameInstance() throws Exception {
        ConcurrentInitializer<Object> initializer = createInitializer();
        Object obj1 = initializer.get();
        Object obj2 = initializer.get();
        assertEquals("Objects must be the same", obj1, obj2);
    }

    @Test
    public void testGetFromMultipleThreads() throws Exception {
        final ConcurrentInitializer<Object> initializer = createInitializer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Object> future1 = executor.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return initializer.get();
            }
        });
        Future<Object> future2 = executor.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return initializer.get();
            }
        });

        Object obj1 = future1.get();
        Object obj2 = future2.get();

        assertEquals("Objects must be the same in concurrent access", obj1, obj2);
        executor.shutdown();
    }
}
