package net.zerobone.concurrentpatterns.timeout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class SchedulerTimeout extends Thread {

    @Override
    public void run() {

        System.out.println("sleeping for 30 seconds");

        try {
            Thread.sleep(30000);
        }
        catch (InterruptedException e) {
            System.out.println("woken up because of an interrupt");
        }

        System.out.println("terminating...");

    }

    public static void main(String[] args) {

        SchedulerTimeout task = new SchedulerTimeout();

        task.start();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(new Runnable() {

            @Override
            public void run() {
                System.out.println("stopping task...");
                task.interrupt();
                scheduler.shutdown();
            }

        }, 5, TimeUnit.SECONDS);

        System.out.println("main terminates");

    }

}