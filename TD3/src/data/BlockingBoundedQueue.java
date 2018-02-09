package data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingBoundedQueue implements MessageQueue {

    private final Message[] queue;
    private final int bound;
    private int in, size;
    
    // The lock that protects this queue .
    private final Lock lock = new ReentrantLock();
    
	// The condition variables that we use to wait for
	// the queue to become non - empty or non - full.
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    public BlockingBoundedQueue(int max) {
        this.queue = new Message[max];
        this.bound = max;
        this.in = 0;
        this.size = 0;
    }

    public boolean isFull() {
        return this.size >= this.bound;
    }

    // Add() is run by producers
    @Override
    public boolean add(Message msg) {
    	
    		lock.lock();
    		try {
    			// Wait for the queue to be not full
	        while (this.isFull()) notFull.awaitUninterruptibly();
	        // We hold the lock

	        boolean wasEmpty = this.isEmpty();
	        
	        this.queue[this.in] = msg;
	        this.in = (this.in + 1) % this.bound;
	        ++this.size;
	        
	        // If the queue just became non-empty, wake up All suspended consumers
	        if (wasEmpty) notEmpty.signalAll();

	        return true;
    		}finally {
    			lock.unlock();
    		}
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    // Remove() is run by consumers 
    @Override
    public Message remove() {

        lock.lock();
        try {
        		// Wait for the queue tu non non-empty
	        while (this.isEmpty()) notEmpty.awaitUninterruptibly();
	        // We hold the lock
	        
	        boolean wasFull = this.isFull();
	        
	        int out = (this.in + this.bound - this.size) % this.bound;
	        Message thing = this.queue[out];
	        this.queue[out] = null;
	        --this.size;
	        
	        // If the queue just became non-full, wake up all suspended producers
	        if (wasFull) notFull.signalAll();

	        return thing;
        }finally {
        		lock.unlock();
        }
    }

}
