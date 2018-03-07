package linkedlists.lockfree;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicMarkableReference;

import contention.abstractions.AbstractCompositionalIntSet;

/**
 * This is a variant of the Harris-Michael algorithm in Java 
 * as presented in the Chapter 9 of the Art of Multiprocessor 
 * Programming by Herlihy and Shavit.
 * 
 * @author Vincent Gramoli
 *
 */

public class NonBlockingLinkedListSet extends AbstractCompositionalIntSet {
	private final Node tail;
	private final Node head;

	public NonBlockingLinkedListSet() {
		tail = new Node(Integer.MAX_VALUE, null);
		head = new Node(Integer.MIN_VALUE, tail);
	}

        class Node {
            public int value;
            public volatile AtomicMarkableReference<Node> next;

            public Node(final int value, final Node next) {
		this.value = value;
		this.next = new AtomicMarkableReference<Node>(next, false);
            }
        }

	class Window {
		public Node pred, curr;

		public Window(Node pred, Node curr) {
			this.pred = pred;
			this.curr = curr;
		}
	}

        /** TODO */
	public Window find(Node head, int value) {
            throw new RuntimeException("TODO");
        }

        /** TODO */
	@Override
	public boolean addInt(int x) {
            throw new RuntimeException("TODO");
	}

        /** TODO */
	@Override
	public boolean removeInt(int x) {
            throw new RuntimeException("TODO");
	}

        /** TODO */
	@Override
	public boolean containsInt(int x) {
            throw new RuntimeException("TODO");
	}

    	@Override
	public void clear() {
		head.next = new AtomicMarkableReference<Node>(tail, false);
	}

	@Override
	public int size() {
		int size = 0;
		boolean[] marked = { false };
		for (Node curr = head.next.getReference(); curr != tail;){
			curr = curr.next.get(marked);
			if (!marked[0]){
				size++;
			}
		}
		return size;
	}
    
	@Override
	public void fill(int range, long size) {
		throw new RuntimeException("unimplemented method");
		// TODO Auto-generated method stub
	}

        @Override
	public Object getInt(int x) {
		throw new RuntimeException("unimplemented method");
		// TODO Auto-generated method stub
	}

	@Override
	public boolean addAll(Collection<Integer> c) {
		throw new RuntimeException("unimplemented method");
		// TODO Auto-generated method stub
	}

	@Override
	public boolean removeAll(Collection<Integer> c) {
		throw new RuntimeException("unimplemented method");
		// TODO Auto-generated method stub
	}

}
