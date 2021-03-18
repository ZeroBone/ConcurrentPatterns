package net.zerobone.concurrentpatterns.scattergather;

import java.util.concurrent.*;

class LatchScatterGather implements Runnable {

    private final int initialValue;

    private final CountDownLatch latch;

    public int result;

    public LatchScatterGather(int initialValue, CountDownLatch latch) {
        assert initialValue >= 1;
        this.initialValue = initialValue;
        this.latch = latch;
    }

    @Override
    public void run() {

        System.out.println("run() with initialValue " + initialValue);

        int sequenceLength = 0;
        int value = initialValue;

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

        result = sequenceLength;

        latch.countDown();

        System.out.println("Done, returning from run(), initial value was " + initialValue);

    }

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(4);

        LatchScatterGather collatz1 = new LatchScatterGather(10001, latch);
        LatchScatterGather collatz2 = new LatchScatterGather(32767, latch);
        LatchScatterGather collatz3 = new LatchScatterGather(32768, latch);
        LatchScatterGather collatz4 = new LatchScatterGather(32769, latch);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

        executor.submit(collatz1);
        executor.submit(collatz2);
        executor.submit(collatz3);
        executor.submit(collatz4);

        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            executor.shutdown();
            return;
        }

        int length1 = collatz1.result;
        int length2 = collatz2.result;
        int length3 = collatz3.result;
        int length4 = collatz4.result;

        executor.shutdown();

        System.out.println("Results:");
        System.out.println("Length of sequence to 10001: " + length1);
        System.out.println("Length of sequence to 32767: " + length2);
        System.out.println("Length of sequence to 32768: " + length3);
        System.out.println("Length of sequence to 32769: " + length4);

    }

}