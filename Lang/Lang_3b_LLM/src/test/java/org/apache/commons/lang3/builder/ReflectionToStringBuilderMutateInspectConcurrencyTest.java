package org.apache.commons.lang3.builder;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ReflectionToStringBuilderMutateInspectConcurrencyTest {

    class TestFixture {
        final private LinkedList<Integer> listField = new LinkedList<Integer>();
        final private Random random = new Random();
        private final int N = 100;

        public TestFixture() {
            synchronized (this) {
                for (int i = 0; i < N; i++) {
                    listField.add(Integer.valueOf(i));
                }
            }
        }

        public synchronized void add() {
            listField.add(Integer.valueOf(random.nextInt(N)));
        }

        public synchronized void delete() {
            listField.remove(Integer.valueOf(random.nextInt(N)));
        }
    }

    class MutatingClient implements Runnable {
        final private TestFixture testFixture;
        final private Random random = new Random();

        public MutatingClient(final TestFixture testFixture) {
            this.testFixture = testFixture;
        }

        @Override
        public void run() {
            if (random.nextBoolean()) {
                testFixture.add();
            } else {
                testFixture.delete();
            }
        }
    }

    class InspectingClient implements Runnable {
        final private TestFixture testFixture;

        public InspectingClient(final TestFixture testFixture) {
            this.testFixture = testFixture;
        }

        @Override
        public void run() {
            ReflectionToStringBuilder.toString(testFixture);
        }
    }

    @Test
    public void testConcurrentMutationAndInspection() throws InterruptedException {
        final TestFixture fixture = new TestFixture();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executor.submit(new MutatingClient(fixture));
            executor.submit(new InspectingClient(fixture));
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }
}
