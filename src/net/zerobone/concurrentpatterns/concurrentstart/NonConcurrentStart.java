package net.zerobone.concurrentpatterns.concurrentstart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

class NonConcurrentStart extends Thread {

    public volatile long ts = 0;

    public volatile boolean done = false;

    @Override
    public void run() {

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

        HashSet<NonConcurrentStart> tasks = new HashSet<>();

        int taskCount = 10;

        for (int i = 0; i < taskCount; i++) {

            NonConcurrentStart task = new NonConcurrentStart();

            tasks.add(task);

            task.start();

        }

        Thread.yield();

        while (!tasks.isEmpty()) {

            Iterator<NonConcurrentStart> it = tasks.iterator();

            while (it.hasNext()) {

                NonConcurrentStart task = it.next();

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