package net.zerobone.concurrentpatterns.producerconsumer.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {

    private final BlockingQueue<Integer> bq;

    public Producer(BlockingQueue<Integer> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {

        Random random = new Random();

        while (true) {

            int value = random.nextInt(2000);

            System.out.println("<<< " + value);

            try {
                bq.put(value);
                Thread.sleep(value);
            }
            catch (InterruptedException e) {
                break;
            }

        }

    }

}
