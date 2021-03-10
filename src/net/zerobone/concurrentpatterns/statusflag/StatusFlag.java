package net.zerobone.concurrentpatterns.statusflag;

public class StatusFlag implements Runnable {

    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {

            System.out.println(System.currentTimeMillis());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {}

        }

        System.out.println("StatusFlag thread terminated");

    }

    public static void main(String[] args) {

        StatusFlag sf = new StatusFlag();
        new Thread(sf).start();

        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stopping thread...");

        sf.stop();

    }

}