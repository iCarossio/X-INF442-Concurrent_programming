package data;

import nodes.Node;

public class BoundedQueue implements MessageQueue {

    private final Message[] queue;
    private final int bound;
    private int in, size;

    public BoundedQueue(int max) {
        this.queue = new Message[max];
        this.bound = max;
        this.in = 0;
        this.size = 0;
    }

    public boolean isFull() {
        return this.size >= this.bound;
    }

    @Override
    public boolean add(Message msg) {
        if (this.isFull()) return false;
        this.queue[this.in] = msg;
        this.in = (this.in + 1) % this.bound;
        ++this.size;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Message remove() {
        while (true) {
            if (!this.isEmpty()) break;
            Node.sleepUninterruptibly(10);
        }
        int out = (this.in + this.bound - this.size) % this.bound;
        Message thing = this.queue[out];
        this.queue[out] = null;
        --this.size;
        return thing;
    }

}
