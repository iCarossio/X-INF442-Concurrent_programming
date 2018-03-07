package linkedlists.lockbased;

import java.util.Collection;
import java.util.Random;
import java.util.Stack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import contention.abstractions.CompositionalIntSet;
import contention.abstractions.CompositionalIterator;
import linkedlists.lockbased.LockCouplingListSortedSet.Node;

/**
 * Linked list implementation of integer set using 
 * optimistic locking as described in: 
 * 
 * Ch.9 of "The art of Multiprocessor 
 * Programming" by Herlihy and Shavit.
 * 
 * @author Francesco Zappa Nardelli
 *
 */
public class OptimisticListSortedSet implements CompositionalIntSet {
 
    /** The first node of the list */ 
    final private Node head;
    /** The thread-private PRNG */
    final private static ThreadLocal<Random> s_random = new ThreadLocal<Random>() {
        @Override
        protected synchronized Random initialValue() {
            return new Random();
        }
    };
	
    public OptimisticListSortedSet() {
        Node min = new Node(Integer.MIN_VALUE);
        Node max = new Node(Integer.MAX_VALUE);
        min.setNext(max);
        head = min;
    }
    
    private boolean validate (Node pred, Node curr) {
    		Node valpred = head;
    		while(valpred.value < pred.value) valpred = valpred.next;
    		return (valpred.value == pred.value && pred.next == curr);
    }

    public boolean addInt(int value) {

	    	while(true) {
	    	    Node pred = head;
	    		Node curr = pred.next;
	    		while (curr.value<value) {
	    			pred = pred.next;
	    			curr = curr.next;
	    		}
	    		pred.lock();
	    		try {
	    			curr.lock();
	    			try{
	    				if (validate(pred, curr)) {
				    	    	if (curr.value == value) {
				    	    		return false;
				    	    	}
				    	    	Node newNode = new Node(value);
				    	    	newNode.next = curr;
				    	    	pred.next = newNode;
				    	    	return true;
	    				}
		    		}finally {
		    			curr.unlock();
		    		}
		    	}finally {
		    		pred.unlock();
		    	}
	    	}
	
	}
	
	/** TODO */
	public boolean removeInt(int value) {

	    	while(true) {
	    	    Node pred = head;
	    		Node curr = pred.next;
	    		while (curr.value<value) {
	    			pred = pred.next;
	    			curr = curr.next;
	    		}
	    		pred.lock();
	    		try {
	    			curr.lock();
	    			try{
	    				if (validate(pred, curr)) {
			    			if (curr.value == value) {
			    				pred.next = curr.next;
			    				return true;
			    			}
			    			return false;
	    				}
	    			}finally {
	    				curr.unlock();
	    			}
	    		}finally {
	    			pred.unlock();
	    		}
	    	}
	}
	
	/** TODO */
	public boolean containsInt(int value) {

		while(true) {
			Node pred = head;
			Node curr = pred.next;
			while (curr.value<value) {
				pred = pred.next;
				curr = curr.next;
			}
			pred.lock();
			try {
				curr.lock();
				try{
					if (validate(pred, curr)) {
						return (curr.value == value);
					}
				}finally {
					curr.unlock();
				}
			}finally {
				pred.unlock();
			}
		}

	}

	
    /**
     * This method cannot be supported with
     * such locking mechanism
     */	
    @Override
    public boolean addAll(Collection<Integer> c) {
       throw new UnsupportedOperationException();
    }

    /**
     * This method cannot be supported with
     * such locking mechanism
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
        int n = 0;
        Node node = head;

        while (node.getNext().getValue() < Integer.MAX_VALUE) {
            n++;
            node = node.getNext();
        }
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
        private volatile Node next;
	final private Lock lock;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
            this.lock = new ReentrantLock();
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

	public void lock() {
	    this.lock.lock();
	}
	
	public void unlock() {
	    this.lock.unlock();
	}
    }
    

    // public class LLIterator implements CompositionalIterator<Integer> {
    //     Node next = head;
    //     Stack<Node> stack = new Stack<Node>();
        
    //     LLIterator() {
    //     	while (next != null) {
    //     		stack.push(next.next);
    //     	}
    //     }

    //     public boolean hasNext() {
    //     	return next != null;
    //     }

    //     public void remove() {
    //     	throw new UnsupportedOperationException();
    //     }

    //     public Integer next() {
    //     	Node node = next;
    //     	next = stack.pop();
    //     	return node.getValue();
    //     }
    // }

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
