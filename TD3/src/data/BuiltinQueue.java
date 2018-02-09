package data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Default implementation using the built-in
// java.util.concurrent.LinkedBlockingQueue.
// This is only provided to test your code.

// DO NOT BASE YOUR IMPLEMENTATIONS ON THIS CLASS

// IT IS AN ERROR TO USE java.util.concurrent.*BlockingQueue
// TO SOLVE THE EXERCISES OF TD3 and 4.

public class BuiltinQueue implements data.MessageQueue {
    private final BlockingQueue<Message> queue;

    public BuiltinQueue(int max) {
        this.queue = new LinkedBlockingQueue<>(max);
    }

    public BuiltinQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public boolean add(Message msg) {
        return this.queue.offer(msg);
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public Message remove() {
        try {
            return this.queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }
}