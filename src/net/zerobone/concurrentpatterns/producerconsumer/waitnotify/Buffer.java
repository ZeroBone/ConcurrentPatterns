package net.zerobone.concurrentpatterns.producerconsumer.waitnotify;

class Buffer {

    private final int[] arr;

    private int left = 0;

    private int right = 0;

    private boolean empty = true;

    public Buffer(int capacity) {

        assert capacity > 0;

        arr = new int[capacity];

    }

    public synchronized boolean isEmpty() {
        return empty;
    }

    public synchronized boolean isFull() {
        return left == right && !empty;
    }

    public synchronized void push(int value) throws InterruptedException {

        while (isFull()) {
            wait();
        }

        arr[right] = value;

        right = (right + 1) % arr.length;

        empty = false;

        notifyAll();

    }

    public synchronized int poll() throws InterruptedException {

        while (isEmpty()) {
            wait();
        }

        int value = arr[left];

        left = (left + 1) % arr.length;

        if (left == right) {
            empty = true;
        }

        notifyAll();

        return value;

    }

}