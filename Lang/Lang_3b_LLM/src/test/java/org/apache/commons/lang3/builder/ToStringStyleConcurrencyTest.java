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

import org.junit.Test;

public class ToStringStyleConcurrencyTest {

    static class CollectionHolder<T extends Collection<?>> {
        T collection;

        CollectionHolder(final T collection) {
            this.collection = collection;
        }
    }

    private static final List<Integer> LIST;
    private static final int LIST_SIZE = 100000;
    private static final int REPEAT = 100;

    static {
        LIST = new ArrayList<Integer>(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            LIST.add(Integer.valueOf(i));
        }
    }

    private void runConcurrencyTest(final CollectionHolder<?> holder) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();

        for (int i = 0; i < REPEAT; i++) {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() {
                    ToStringBuilder.reflectionToString(holder);
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
    public void testConcurrentAccessCopyOnWriteArrayList() throws Exception {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>(LIST);
        runConcurrencyTest(new CollectionHolder<CopyOnWriteArrayList<Integer>>(list));
    }

    @Test
    public void testConcurrentAccessArrayList() throws Exception {
        ArrayList<Integer> list = new ArrayList<Integer>(LIST);
        runConcurrencyTest(new CollectionHolder<ArrayList<Integer>>(list));
    }

    @Test
    public void testConcurrentAccessLinkedList() throws Exception {
        LinkedList<Integer> list = new LinkedList<Integer>(LIST);
        runConcurrencyTest(new CollectionHolder<LinkedList<Integer>>(list));
    }
}