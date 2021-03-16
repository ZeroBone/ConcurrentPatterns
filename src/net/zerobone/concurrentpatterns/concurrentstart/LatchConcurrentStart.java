package net.zerobone.concurrentpatterns.concurrentstart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

class LatchConcurrentStart extends Thread {

    private final CountDownLatch latch;

    public volatile long ts = 0;

    public volatile boolean done = false;

    public LatchConcurrentStart(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {

        System.out.println("Task thread started, waiting for command to start...");

        try {
            latch.await();
        }
        catch (InterruptedException e) {
            ts = -1;
            e.printStackTrace();
            return;
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

        final CountDownLatch latch = new CountDownLatch(1);

        HashSet<LatchConcurrentStart> tasks = new HashSet<>();

        int taskCount = 10;

        for (int i = 0; i < taskCount; i++) {

            LatchConcurrentStart task = new LatchConcurrentStart(latch);

            tasks.add(task);

            task.start();

        }

        System.out.println("Threads lined-up, starting...");

        latch.countDown();

        while (!tasks.isEmpty()) {

            Iterator<LatchConcurrentStart> it = tasks.iterator();

            while (it.hasNext()) {

                LatchConcurrentStart task = it.next();

                if (task.done) {
                    System.out.println("Task finished at: " + task.ts);
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