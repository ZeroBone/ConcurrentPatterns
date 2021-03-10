package net.zerobone.concurrentpatterns.producerconsumer.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ProducerConsumer {

    public static void main(String[] args) {

        BlockingQueue<Integer> bq = new ArrayBlockingQueue<>(4);

        Runnable producer = new Producer(bq);

        Runnable consumer = new Consumer(bq);

        new Thread(consumer).start();
        new Thread(consumer).start();
        // new Thread(consumer).start();

        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();

    }

}