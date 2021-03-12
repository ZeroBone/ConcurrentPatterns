package net.zerobone.concurrentpatterns.badlock;

import java.util.concurrent.atomic.AtomicBoolean;

class UnlockFirstVolatile {

    private static volatile boolean used = true;

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

    // guaranteed to terminate
    public static void main(String[] args) {

        final AtomicBoolean lockAllowed = new AtomicBoolean();

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (!lockAllowed.get()) {
                    Thread.yield();
                }

                UnlockFirstVolatile l = new UnlockFirstVolatile();
                System.out.println("[thread]: " + used);
                l.lock();

            }

        }).start();

        UnlockFirstVolatile l = new UnlockFirstVolatile();
        System.out.println("[main]: " + used);
        l.unlock();
        System.out.println("[main]: " + used);

        lockAllowed.set(true);

        System.out.println("[main]: " + used);

    }

}