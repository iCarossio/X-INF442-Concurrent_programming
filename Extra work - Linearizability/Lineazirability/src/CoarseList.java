import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseList<E> {

	public class Node<E> {

		final private E item;
		final private int key;
		private Node<E> next;

		public Node(E obj, Node<E> next) {
			this.item = obj;
			this.key = item.hashCode();
			this.next = next;
		}

		public Node(E obj) {
			this(obj, null);
		}

		public Node(int key) {
			this.key = key;
			this.item = null;
			this.next = null;
		}
	}

	private Node<E> head;
	private Lock lock = new ReentrantLock();

	public CoarseList() {
		head = new Node<E>(Integer.MIN_VALUE);
		head.next = new Node<E>(Integer.MAX_VALUE);
	}

	public boolean add(E item) {
		Node<E> pred, curr;
		int key = item.hashCode();

		lock.lock();
		try {
			pred = head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			if (key == curr.key) {
				return false;
			} else {
				Node<E> node = new Node<E>(item);
				node.next = curr;
				pred.next = node;
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean remove(E item) {
		Node<E> pred, curr;
		int key = item.hashCode();

		lock.lock();
		try {
			pred = head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			if (key == curr.key) {
				pred.next = curr.next;
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean contains(E item) {
		Node<E> pred, curr;
		int key = item.hashCode();

		lock.lock();
		try {
			pred = head;
			curr = pred.next;
			while (curr.key < key) {
				pred = curr;
				curr = curr.next;
			}
			if (key == curr.key)
				return true;
			else
				return false;
		} finally {
			lock.unlock();
		}
	}

}