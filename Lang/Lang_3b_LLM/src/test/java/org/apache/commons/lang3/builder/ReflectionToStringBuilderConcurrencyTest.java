package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionToStringBuilderConcurrencyTest {

    static class CollectionHolder<T extends Collection<?>> {
        T collection;

        CollectionHolder(final T collection) {
            this.collection = collection;
        }
    }

    private static final int DATA_SIZE = 100000;
    private static final int REPEAT = 100;

    private void runConcurrencyTest(final CollectionHolder<?> holder) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();

        for (int i = 0; i < REPEAT; i++) {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() {
                    ReflectionToStringBuilder.toString(holder);
                    return null;
                }
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);
        for (Future<Void> future : futures) {
            future.get();
        }
        executor.shutdown();
    }

    @Test
    public void testConcurrentCopyOnWriteArrayList() throws Exception {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
        for (int i = 0; i < DATA_SIZE; i++) {
            list.add("value" + i);
        }
        runConcurrencyTest(new CollectionHolder<CopyOnWriteArrayList<String>>(list));
    }

    @Test
    public void testConcurrentArrayList() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < DATA_SIZE; i++) {
            list.add("value" + i);
        }
        runConcurrencyTest(new CollectionHolder<ArrayList<String>>(list));
    }

    @Test
    public void testConcurrentLinkedList() throws Exception {
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < DATA_SIZE; i++) {
            list.add("value" + i);
        }
        runConcurrencyTest(new CollectionHolder<LinkedList<String>>(list));
    }
}
