package linkedlists.lockbased;

import java.util.Collection;
import java.util.Random;
import java.util.Stack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import contention.abstractions.CompositionalIntSet;
import contention.abstractions.CompositionalIterator;

/**
 * Coarse grained linked list
 * 
 * @author Francesco Zappa Nardelli
 *
 */
public class LockedLinkedListSortedSet implements CompositionalIntSet {
 
    /** The first node of the list */ 
    final private Node head;

    /** The global lock */
    final private Lock lock;

    /** The thread-private PRNG */
    final private static ThreadLocal<Random> s_random = new ThreadLocal<Random>() {
        @Override
        protected synchronized Random initialValue() {
            return new Random();
        }
    };
	
    public LockedLinkedListSortedSet() {
	lock = new ReentrantLock();
	
        Node min = new Node(Integer.MIN_VALUE);
        Node max = new Node(Integer.MAX_VALUE);
        min.setNext(max);
        head = min;
    }

    public boolean addInt(int value) {
	lock.lock();
	
        Node pred = head;
	Node curr = pred.getNext();

	while(curr.getValue() < value) {
	    pred = curr;
	    curr = curr.getNext();
	}
	
	if (curr.getValue() == value) {
	    lock.unlock();
	    return false;
	}
	
	Node newNode = new Node(value, curr);
	pred.setNext(newNode);
	lock.unlock();
	return true;
    }

    public boolean removeInt(int value) {
	lock.lock();

        Node pred = head;
  	Node curr = pred.getNext();

	while (curr.getValue() < value) {
	    pred = curr; 
	    curr = curr.getNext();
	}

	if (curr.getValue() == value) {
	    pred.setNext(curr.getNext());
	    lock.unlock();
	    return true;
	}
	
	lock.unlock();
	return false;
    }

    public boolean containsInt(int value) {
	lock.lock();
	
        Node pred = head;
	Node curr = pred.getNext();

	while(curr.getValue() < value) {
	    pred = curr;
	    curr = curr.getNext();
	}

	if (curr.getValue() == value) {
	    lock.unlock();
	    return true;
	} else {
	    lock.unlock();
	    return false;
	}
    }
    
    /**
     * Unimplemented.
     */	
    @Override
    public boolean addAll(Collection<Integer> c) {
       throw new UnsupportedOperationException();
    }

    /**
     * Unimplemented.
     */	
    @Override
    public boolean removeAll(Collection<Integer> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is not thread-safe. It cannot 
     * be made atomic with such locking mechanism
     */
    public int size() {
	lock.lock();
	
        int n = 0;
        Node node = head;

        while (node.getNext().getValue() < Integer.MAX_VALUE) {
            n++;
            node = node.getNext();
        }
	lock.unlock();
        return n;
    }

    @Override
    public void fill(final int range, final long size) {
	while (this.size() < size) {
	    this.addInt(s_random.get().nextInt(range));
	}
    }
    
    public class Node {

        final private int value;
        private Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
	}

        public Node(int value) {
            this(value, null);
        }

        public int getValue() {
            return value;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getNext() {
            return next;
        }

    }
    

    public void clear() {
	Node max = new Node(Integer.MAX_VALUE);
	head.setNext(max);
    }

    @Override
    public Object getInt(int x) {
	throw new UnsupportedOperationException();	
    }
    
    @Override
    public Object putIfAbsent(int x, int y) {
	throw new UnsupportedOperationException();
    }
}
