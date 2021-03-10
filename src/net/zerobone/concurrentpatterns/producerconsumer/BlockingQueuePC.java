package net.zerobone.concurrentpatterns.producerconsumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BlockingQueuePC {

    public static void main(String[] args) {

        BlockingQueue<Integer> bq = new ArrayBlockingQueue<>(4);

        Runnable producer = new Runnable() {

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

        };

        Runnable consumer = new Runnable() {

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

        };

        new Thread(consumer).start();
        new Thread(consumer).start();
        // new Thread(consumer).start();

        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();

    }

}