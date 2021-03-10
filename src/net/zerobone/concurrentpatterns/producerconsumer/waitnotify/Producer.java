package net.zerobone.concurrentpatterns.producerconsumer.waitnotify;

import java.util.Random;

class Producer implements Runnable {

    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        Random random = new Random();

        while (true) {

            int value = random.nextInt(2000);

            System.out.println("<<< " + value);

            try {
                buffer.push(value);
                Thread.sleep(value);
            }
            catch (InterruptedException e) {
                break;
            }

        }

    }

}