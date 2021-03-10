package net.zerobone.concurrentpatterns.producerconsumer.waitnotify;

class Consumer implements Runnable {

    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {

        while (true) {

            int value;

            try {
                value = buffer.poll();
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