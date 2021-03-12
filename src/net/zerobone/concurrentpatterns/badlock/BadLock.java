package net.zerobone.concurrentpatterns.badlock;

class BadLock {

    private static boolean used = true;

    public synchronized void lock() {
        System.out.println("entering lock(): " + BadLock.used);
        while (used) {
            System.out.println("iter");
            Thread.yield();
        }
    }

    public synchronized void unlock() {
        used = false;
    }

    // will not terminate if the assignment to the used variable is not visible
    // for the created thread
    public static void main(String[] args) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                BadLock l = new BadLock();
                System.out.println("[thread]: " + BadLock.used);
                l.lock();
            }

        }).start();

        BadLock l = new BadLock();
        System.out.println("[main]: " + BadLock.used);
        l.unlock();
        System.out.println("[main]: " + BadLock.used);

    }

}