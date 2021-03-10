package net.zerobone.concurrentpatterns.timeout;

import java.util.concurrent.*;

class FutureCancelTimeout implements Runnable {

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

        FutureCancelTimeout task = new FutureCancelTimeout();

        ExecutorService pool = Executors.newFixedThreadPool(1);

        Future<?> future = pool.submit(task);

        try {
            future.get(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        catch (TimeoutException e) {
            boolean cancelled = future.cancel(true);
            System.out.println("Cancelled: " + cancelled);
        }
        finally {
            pool.shutdown();
        }

    }

}