package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.ThreadFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code BasicThreadFactory}.
 */
public class BasicThreadFactoryTest {
    
    private static final String PATTERN = "testThread-%d";

    private BasicThreadFactory.Builder builder;

    @Before
    public void setUp() {
        builder = new BasicThreadFactory.Builder();
    }

    private void checkFactoryDefaults(final BasicThreadFactory factory) {
        assertNull("Got a naming pattern", factory.getNamingPattern());
        assertNull("Got an exception handler", factory.getUncaughtExceptionHandler());
        assertNull("Got a priority", factory.getPriority());
        assertNull("Got a daemon flag", factory.getDaemonFlag());
        assertNotNull("No wrapped factory", factory.getWrappedFactory());
    }

    @Test
    public void testBuilderDefaults() {
        BasicThreadFactory factory = builder.build();
        checkFactoryDefaults(factory);
    }

    @Test
    public void testNamingPattern() {
        BasicThreadFactory factory = builder.namingPattern(PATTERN).build();
        assertEquals("Naming pattern not set", PATTERN, factory.getNamingPattern());
    }

    @Test
    public void testDaemonFlag() {
        BasicThreadFactory factory = builder.daemon(true).build();
        assertTrue("Daemon flag not set", factory.getDaemonFlag());
    }

    @Test
    public void testPriority() {
        BasicThreadFactory factory = builder.priority(Thread.MAX_PRIORITY).build();
        assertEquals("Priority not set", Integer.valueOf(Thread.MAX_PRIORITY), factory.getPriority());
    }

    @Test
    public void testThreadCreation() {
        BasicThreadFactory factory = builder.namingPattern(PATTERN).build();
        Thread t = factory.newThread(new Runnable() {
            public void run() {}
        });
        assertTrue("Name does not match pattern", t.getName().startsWith("testThread-"));
    }

    @Test
    public void testWrappedFactory() {
        ThreadFactory mockFactory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
        BasicThreadFactory factory = builder.wrappedFactory(mockFactory).build();
        assertSame("Wrapped factory not set", mockFactory, factory.getWrappedFactory());
    }
}