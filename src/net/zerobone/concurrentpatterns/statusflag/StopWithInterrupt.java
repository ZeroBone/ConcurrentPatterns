package net.zerobone.concurrentpatterns.statusflag;

import java.io.IOException;

public class StopWithInterrupt extends Thread {

    @Override
    public void run() {

        main:
        while (true) {

            for (int i = 0; i < 100000000; i++) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupt occurred in loop, shutting down");
                    break main;
                }

            }

            try {
                Thread.sleep(200);
            }
            catch (InterruptedException e) {
                System.out.println("Interrupt occurred while sleeping, shutting down");
                break;
            }

        }

        System.out.println("Terminating thread...");

    }

    public static void main(String[] args) {

        StopWithInterrupt swi = new StopWithInterrupt();

        swi.start();

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    int character;

                    try {
                        character = System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                    System.out.println("You entered: " + (char)character);

                    if (character == 's') {
                        System.out.println("Calling interrupt()");
                        swi.interrupt();
                        break;
                    }

                }

            }

        }).start();

    }

}