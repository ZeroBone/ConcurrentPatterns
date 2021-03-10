package net.zerobone.concurrentpatterns.producerconsumer.waitnotify;

class ProducerConsumer {

    public static void main(String[] args) {

        Buffer buffer = new Buffer(4);

        Producer producer = new Producer(buffer);

        Consumer consumer = new Consumer(buffer);

        new Thread(consumer).start();
        new Thread(consumer).start();
        // new Thread(consumer).start();

        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();

    }

}