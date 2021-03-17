package net.zerobone.concurrentpatterns.scattergather;

class JoinScatterGather extends Thread {

    private final int initialValue;

    public int result = 0;

    public JoinScatterGather(int initialValue) {
        assert initialValue >= 1;
        this.initialValue = initialValue;
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

        System.out.println("Done, returning from run(), initial value was " + initialValue);

    }

    public static void main(String[] args) {

        JoinScatterGather collatz1 = new JoinScatterGather(10001);
        JoinScatterGather collatz2 = new JoinScatterGather(32767);
        JoinScatterGather collatz3 = new JoinScatterGather(32768);
        JoinScatterGather collatz4 = new JoinScatterGather(32769);

        collatz1.start();
        collatz2.start();
        collatz3.start();
        collatz4.start();

        try {
            collatz1.join();
            collatz2.join();
            collatz3.join();
            collatz4.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        int length1 = collatz1.result;
        int length2 = collatz2.result;
        int length3 = collatz3.result;
        int length4 = collatz4.result;

        System.out.println("Results:");
        System.out.println("Length of sequence to 10001: " + length1);
        System.out.println("Length of sequence to 32767: " + length2);
        System.out.println("Length of sequence to 32768: " + length3);
        System.out.println("Length of sequence to 32769: " + length4);

    }

}