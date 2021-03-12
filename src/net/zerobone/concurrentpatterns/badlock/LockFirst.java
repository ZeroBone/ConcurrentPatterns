package net.zerobone.concurrentpatterns.badlock;

import java.util.concurrent.atomic.AtomicBoolean;

class LockFirst {

    private static boolean used = true;

    public synchronized void lock(AtomicBoolean unlockAllowed) {
        System.out.println("entering lock(): " + used);
        while (used) {
            System.out.println("iter");
            unlockAllowed.set(true);
            Thread.yield();
        }
    }

    public synchronized void unlock() {
        used = false;
    }

    // terminates if the cache of the used variable is shared between threads
    // if the value is not written-through, this program will run forever
    public static void main(String[] args) {

        final AtomicBoolean unlockAllowed = new AtomicBoolean();

        new Thread(new Runnable() {

            @Override
            public void run() {
                LockFirst l = new LockFirst();
                System.out.println("[thread]: " + used);
                l.lock(unlockAllowed);
            }

        }).start();

        do {
            Thread.yield();
        } while (!unlockAllowed.get());

        LockFirst l = new LockFirst();
        System.out.println("[main]: " + used);
        l.unlock();
        System.out.println("[main]: " + used);

    }

}