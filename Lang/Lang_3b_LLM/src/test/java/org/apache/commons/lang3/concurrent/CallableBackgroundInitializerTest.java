package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * Test class for {@code CallableBackgroundInitializer}
 */
public class CallableBackgroundInitializerTest  {

    private static final Integer RESULT = Integer.valueOf(42);

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullCallable() {
        new CallableBackgroundInitializer<Integer>(null);
    }

    @Test
    public void testInitialize() throws Exception {
        CallableBackgroundInitializer<Integer> init =
                new CallableBackgroundInitializer<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                return RESULT;
            }
        });
        init.start();
        assertEquals("Wrong result", RESULT, init.get());
    }

    @Test
    public void testInitializeWithExecutor() throws Exception {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        CallableBackgroundInitializer<Integer> init =
                new CallableBackgroundInitializer<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                return RESULT;
            }
        }, exec);
        assertEquals("Executor not set", exec, init.getExternalExecutor());
        init.start();
        assertEquals("Wrong result", RESULT, init.get());
    }
} 
