package net.zerobone.concurrentpatterns.timeout;

class SimpleTimeout extends Thread {

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

        SimpleTimeout st = new SimpleTimeout();

        st.start();

        System.out.println("Main thread - waiting 5 seconds");

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException ignored) {}

        st.interrupt();

        System.out.println("Main thread terminates");

    }

}