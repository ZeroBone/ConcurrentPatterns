package net.zerobone.concurrentpatterns.producerconsumer.blockingqueue;

import java.util.concurrent.BlockingQueue;

class Consumer implements Runnable {

    private final BlockingQueue<Integer> bq;

    public Consumer(BlockingQueue<Integer> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {

        while (true) {

            int value;

            try {
                value = bq.take();
            }
            catch (InterruptedException e) {
                break;
            }

            System.out.println(">>> " + value);

            try {
                Thread.sleep(value);
            }
            catch (InterruptedException e) {
                break;
            }

        }

    }

}