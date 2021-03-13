package net.zerobone.concurrentpatterns.cheaprwlock;

class CheapCounter {

    // it is important to use integer here, as in Java language specification,
    // it is guaranteed that integers are written-to atomically
    // the code is not thread-safe if we use double or long here, for example,
    // and don't declare the variable as volatile
    // if we declare the variable as volatile, it would be ok to use other types like long or double
    private volatile int value;

    public int get() {
        return value;
    }

    // it is guaranteed that all increment/incrementBy operations will take place and that
    // the value of the counter value will not get corrupted
    public synchronized void increment() {
        value++;
    }

    public synchronized void incrementBy(int diff) {
        value += diff;
    }

    public static void main(String[] args) {

        final CheapCounter counter = new CheapCounter();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    int before = counter.get();
                    counter.increment();
                    int after = counter.get();
                    // it is not guaranteed that after == before + 1,
                    // because counter.get() returns the latest value of the counter without sync

                    System.out.printf("[1]: Before: %5d After %5d Ok: %b\n", before, after, after == before + 1);

                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }
            }

        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    int before = counter.get();
                    counter.incrementBy(2);
                    int after = counter.get();
                    // it is not guaranteed that after == before + 2,
                    // because counter.get() returns the latest value of the counter without sync

                    System.out.printf("[2]: Before: %5d After %5d Ok: %b\n", before, after, after == before + 2);

                    try {
                        Thread.sleep(7);
                    }
                    catch (InterruptedException e) {
                        break;
                    }

                }
            }

        }).start();

    }

}