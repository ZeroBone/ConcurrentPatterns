package net.zerobone.concurrentpatterns.badlock;

import java.util.concurrent.atomic.AtomicBoolean;

public class LockFirstVolatile {

    private static volatile boolean used = true;

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

    // terminates guaranteed
    public static void main(String[] args) {

        final AtomicBoolean unlockAllowed = new AtomicBoolean();

        new Thread(new Runnable() {

            @Override
            public void run() {
                LockFirstVolatile l = new LockFirstVolatile();
                System.out.println("[thread]: " + used);
                l.lock(unlockAllowed);
            }

        }).start();

        do {
            Thread.yield();
        } while (!unlockAllowed.get());

        LockFirstVolatile l = new LockFirstVolatile();
        System.out.println("[main]: " + used);
        l.unlock();
        System.out.println("[main]: " + used);

    }

}