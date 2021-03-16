package net.zerobone.concurrentpatterns.concurrentstart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ConcurrentStart extends Thread {

    private final Object await;

    private final AtomicInteger taskCounter;

    public volatile long ts = 0;

    public volatile boolean done = false;

    public ConcurrentStart(Object await, AtomicInteger taskCounter) {
        this.await = await;
        this.taskCounter = taskCounter;
    }

    @Override
    public void run() {

        System.out.println("Task thread started.");

        synchronized (await) {

            // it is important to run this inside synchronized
            // to prevent the notify of happening too early
            taskCounter.decrementAndGet();

            try {
                await.wait();
            }
            catch (InterruptedException e) {
                ts = -1;
                taskCounter.incrementAndGet();
                e.printStackTrace();
                return;
            }

        }

        ts = System.currentTimeMillis();

        // do some work

        int value = 100 + new Random().nextInt(9900);
        int initialValue = value;
        int sequenceLength = 0;

        while (value != 1) {
            if (value % 2 == 0) {
                value /= 2;
                sequenceLength++;
            }
            else {
                value = (value * 3 + 1) / 2;
                sequenceLength += 2;
            }
        }

        done = true;

        System.out.println("Collatz sequence starting from " + initialValue + " has length " + sequenceLength);

    }

    public static void main(String[] args) {

        final Object await = new Object();

        HashSet<ConcurrentStart> tasks = new HashSet<>();

        int taskCount = 10;

        AtomicInteger taskCounter = new AtomicInteger(taskCount);

        for (int i = 0; i < taskCount; i++) {

            ConcurrentStart task = new ConcurrentStart(await, taskCounter);

            tasks.add(task);

            task.start();

        }

        System.out.println("Threads lined-up, starting...");

        while (taskCounter.get() != 0) {
            Thread.yield();
            try {
                //noinspection BusyWait
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (await) {
            await.notifyAll();
        }

        while (!tasks.isEmpty()) {

            Iterator<ConcurrentStart> it = tasks.iterator();

            while (it.hasNext()) {

                ConcurrentStart task = it.next();

                if (task.done) {
                    System.out.println("Task started working at: " + task.ts);
                    it.remove();
                }

            }

            Thread.yield();

            try {
                //noinspection BusyWait
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }

        System.out.println("All tasks finished.");

    }

}