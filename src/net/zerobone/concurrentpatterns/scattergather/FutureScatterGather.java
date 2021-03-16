package net.zerobone.concurrentpatterns.scattergather;

import java.util.concurrent.*;

class FutureScatterGather implements Callable<Integer> {

    private final int initialValue;

    public FutureScatterGather(int initialValue) {
        assert initialValue >= 1;
        this.initialValue = initialValue;
    }

    @Override
    public Integer call() throws Exception {

        System.out.println("call() with initialValue " + initialValue);

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

        System.out.println("Done, returning from call(), initial value was " + initialValue);

        return sequenceLength;

    }

    public static void main(String[] args) {

        Callable<Integer> collatz1 = new FutureScatterGather(10001);
        Callable<Integer> collatz2 = new FutureScatterGather(32767);
        Callable<Integer> collatz3 = new FutureScatterGather(32768);
        Callable<Integer> collatz4 = new FutureScatterGather(32769);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Future<Integer> result1 = executor.submit(collatz1);
        Future<Integer> result2 = executor.submit(collatz2);
        Future<Integer> result3 = executor.submit(collatz3);
        Future<Integer> result4 = executor.submit(collatz4);

        int length1;
        int length2;
        int length3;
        int length4;


        try {
            // order in which we get the result doesn't matter
            // it doesn't determine the order in which the tasks will be executed
            // tasks are executed in the order in which they were submitted
            length2 = result2.get();
            length1 = result1.get();
            length3 = result3.get();
            length4 = result4.get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            executor.shutdown();
            return;
        }

        executor.shutdown();

        System.out.println("Results:");
        System.out.println("Length of sequence to 10001: " + length1);
        System.out.println("Length of sequence to 32767: " + length2);
        System.out.println("Length of sequence to 32768: " + length3);
        System.out.println("Length of sequence to 32769: " + length4);

    }

}