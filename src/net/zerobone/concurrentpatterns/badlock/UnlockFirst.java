package net.zerobone.concurrentpatterns.badlock;

import java.util.concurrent.atomic.AtomicBoolean;

class UnlockFirst {

    private static boolean used = true;

    public synchronized void lock() {
        System.out.println("entering lock(): " + used);
        while (used) {
            System.out.println("iter");
            Thread.yield();
        }
    }

    public synchronized void unlock() {
        used = false;
    }

    // doesn't terminate because used is not volatile
    // can terminate if the assignment to used was a write-through assignment
    public static void main(String[] args) {

        final AtomicBoolean lockAllowed = new AtomicBoolean();

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (!lockAllowed.get()) {
                    Thread.yield();
                }

                UnlockFirst l = new UnlockFirst();
                System.out.println("[thread]: " + used);
                l.lock();

            }

        }).start();

        UnlockFirst l = new UnlockFirst();
        System.out.println("[main]: " + used);
        l.unlock();
        System.out.println("[main]: " + used);

        lockAllowed.set(true);

        System.out.println("[main]: " + used);

    }

}