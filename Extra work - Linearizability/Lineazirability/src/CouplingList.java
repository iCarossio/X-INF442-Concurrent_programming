import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CouplingList<E> {

	public class Node<E> {

		final private E item;
		final private int key;
		private Node<E> next;
		final private Lock nodeLock; // Nouveauté : un Lock par neœud

		public Node(E obj, Node<E> next) {
			this.item = obj;
			this.key = item.hashCode();
			this.next = next;
			this.nodeLock = new ReentrantLock();
		}

		public Node(E obj) {
			this(obj, null);
		}

		public Node(int key) {
			this.key = key;
			this.item = null;
			this.next = null;
			this.nodeLock = new ReentrantLock();
		}

		// Implémentation des méthodes lock() et unlock() pour les Nodes
		public void lock() {
			this.nodeLock.lock();
		}

		public void unlock() {
			this.nodeLock.unlock();
		}
	}

	private Node<E> head;
	// Note : Il n'y a plus de lock pour la liste puisqu'on utilise ceux des Nodes

	public CouplingList() {
		head = new Node<E>(Integer.MIN_VALUE);
		head.next = new Node<E>(Integer.MAX_VALUE);
	}

	public boolean add(E item) {
		int key = item.hashCode();
		Node<E> pred = head;

		pred.lock(); // Prend le verrou sur chaque paire (pred, curr) visitée…
		try {
			Node<E> curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock(); }
				if (curr.key == key) {
					return false; 
				}
				Node<E> newNode = new Node<E> (item);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			} finally {
				curr.unlock(); 
			}
		} finally {
			pred.unlock(); 
		} 
	}

	public boolean remove(E item) {
		Node<E> pred = null, curr = null;
		int key = item.hashCode();
		pred = head;

		pred.lock();
		try {
			curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					pred.next = curr.next;
					return true; }
				return false;
			} finally {
				curr.unlock(); 
			}
		} finally {
			pred.unlock(); 
		} 
	}

	public boolean contains(E item) {
		Node<E> pred , curr;
		int key = item.hashCode();
		pred = head;

		pred.lock();
		curr = pred.next;
		try {
			while (curr.key < key) {
				pred.unlock();
				pred = curr;
				pred.lock();
				curr = curr.next;
			}
			if (curr.key == key)
				return true;
			else
				return false;
		} finally {
			pred.unlock();
		}
	}

}