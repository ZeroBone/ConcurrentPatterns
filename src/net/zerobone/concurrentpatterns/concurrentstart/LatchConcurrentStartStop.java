package net.zerobone.concurrentpatterns.concurrentstart;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

class LatchConcurrentStartStop extends Thread {

    private final CountDownLatch startLatch;

    private final CountDownLatch stopLatch;

    public volatile long ts = 0;

    public LatchConcurrentStartStop(CountDownLatch startLatch, CountDownLatch stopLatch) {
        this.startLatch = startLatch;
        this.stopLatch = stopLatch;
    }

    @Override
    public void run() {

        System.out.println("Task thread started, waiting for command to start...");

        try {
            startLatch.await();
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

        System.out.println("Collatz sequence starting from " + initialValue + " has length " + sequenceLength);

        stopLatch.countDown();

    }

    public static void main(String[] args) {

        int taskCount = 10;

        final CountDownLatch startLatch = new CountDownLatch(1);

        final CountDownLatch stopLatch = new CountDownLatch(taskCount);

        LatchConcurrentStartStop[] tasks = new LatchConcurrentStartStop[taskCount];

        for (int i = 0; i < taskCount; i++) {

            tasks[i] = new LatchConcurrentStartStop(startLatch, stopLatch);

            tasks[i].start();

        }

        System.out.println("Threads lined-up, starting...");

        startLatch.countDown();

        try {
            stopLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        for (LatchConcurrentStartStop task : tasks) {
            System.out.println("Task started working at: " + task.ts);
        }

        System.out.println("All tasks finished.");

    }

}