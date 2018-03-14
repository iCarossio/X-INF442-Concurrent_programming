import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LazyList<E> {

	public class Node<E> {

		final private E item;
		final private int key;
		private Node<E> next;
		final private Lock nodeLock;
		private volatile boolean marked; // Nouveauté : marked est faux si le noeud est dans la Liste

		public Node(E obj, Node<E> next) {
			this.item = obj;
			this.key = item.hashCode();
			this.next = next;
			this.nodeLock = new ReentrantLock();
			this.marked = false;
		}

		public Node(E obj) {
			this(obj, null);
		}

		public Node(int key) {
			this.key = key;
			this.item = null;
			this.next = null;
			this.nodeLock = new ReentrantLock();
			this.marked = false;
		}

		// On implémente toujours les méthodes lock() et unlock() pour les Nodes
		public void lock() {
			this.nodeLock.lock();
		}

		public void unlock() {
			this.nodeLock.unlock();
		}
	}

	private Node<E> head;

	public LazyList() {
		head = new Node<E>(Integer.MIN_VALUE);
		head.next = new Node<E>(Integer.MAX_VALUE);
	}

	// Nouveauté : validate utilise le champ market et n'a plus besoin de parcourir Liste
	private boolean validate (Node<E> pred , Node<E> curr) {
		return !pred.marked && !curr.marked && pred.next == curr;
	}

	// Idem que optimiste
	public boolean add(E item) {
		int key = item.hashCode();

		while (true) {
			Node<E> pred = head;
			Node<E> curr = head.next;
			while (curr.key < key) {
				pred = curr; curr = curr.next; 
			}

			pred.lock();
			try {
				curr.lock();
				try {
					if (validate(pred, curr)) {
						if (curr.key == key)
							return false;
						else {
							Node<E> node = new Node<E> (item);
							node.next = curr;
							pred.next = node;
							return true; 
						} 
					}
				} finally { 
					curr.unlock(); 
				}
			} finally { 
				pred.unlock(); 
			}

		}
	}

	// Léger changement : *
	public boolean remove(E item) {
		int key = item.hashCode();

		while (true) {
			Node<E> pred = head;
			Node<E> curr = head.next;
			while (curr.key < key) {
				pred = curr; curr = curr.next; 
			}

			pred.lock();
			try { 
				curr.lock();
				try {
					if (validate (pred , curr)) {
						if (curr.key != key) {
							return false;
						} else {
							curr.marked = true; // * On marque qu'on a supprimé logiquement curr de la Liste
							pred.next = curr.next;
							return true; } 
					}
				} finally { 
					curr.unlock(); 
				}
			} finally { 
				pred.unlock(); 
			}

		}
	}

	// contains() est désormais lockfree !
	public boolean contains (E item) {
		int key = item.hashCode();
		Node<E> curr = head;
		while (curr.key < key)
			curr = curr.next;
		return curr.key == key && !curr.marked;
	}
}