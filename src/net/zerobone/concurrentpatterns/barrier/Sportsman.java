package net.zerobone.concurrentpatterns.barrier;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

class Sportsman extends Thread {

    private final int id;

    private final CyclicBarrier barrier;

    public volatile long endTime;

    public Sportsman(int id, CyclicBarrier barrier) {
        this.id = id;
        this.barrier = barrier;
    }

    @Override
    public void run() {

        while (true) {

            System.out.println("[" + id + "]: Running!");

            long steps = 0;

            do {
                long value = 9000000 + new Random().nextInt(999999);

                while (value != 1) {
                    if (value % 2 == 0) {
                        value /= 2;
                        steps++;
                    }
                    else {
                        value = (value * 3 + 1) / 2;
                        steps += 2;
                    }
                }
            } while (System.nanoTime() % 769 != 0);

            endTime = System.currentTimeMillis();

            System.out.println("[" + id + "]: Reached the finish! Steps: " + steps);

            try {
                barrier.await();
            }
            catch (InterruptedException | BrokenBarrierException e) {
                System.err.println("[" + id + "]: Error:");
                e.printStackTrace();
                break;
            }

        }

    }

    public static void main(String[] args) {

        final Sportsman[] sportsmen = new Sportsman[4];

        final AtomicLong startTime = new AtomicLong(System.currentTimeMillis());

        CyclicBarrier barrier = new CyclicBarrier(sportsmen.length, new Runnable() {

            @Override
            public void run() {

                System.out.println("All sportsmen arrived at the barrier!");

                Arrays.sort(sportsmen, new Comparator<Sportsman>() {

                    @Override
                    public int compare(Sportsman s1, Sportsman s2) {

                        return (int)(s1.endTime - s2.endTime);

                    }

                });

                System.out.println("Scoreboard:");

                long startTimeLong = startTime.get();

                for (Sportsman s: sportsmen) {

                    long time = s.endTime - startTimeLong;

                    System.out.println("Sportsman " + s.id + " time: " + time);

                }

                try {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }

                System.out.println("Starting a new round!");

                startTime.set(System.currentTimeMillis());

            }

        });

        for (int i = 0; i < sportsmen.length; i++) {
            sportsmen[i] = new Sportsman(i + 1, barrier);
            sportsmen[i].start();
        }

    }

}