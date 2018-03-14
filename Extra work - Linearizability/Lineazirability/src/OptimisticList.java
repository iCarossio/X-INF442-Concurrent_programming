import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimisticList<E> {

	public class Node<E> {

		final private E item;
		final private int key;
		private Node<E> next;
		final private Lock nodeLock;

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

		// On implémente toujours les méthodes lock() et unlock() pour les Nodes
		public void lock() {
			this.nodeLock.lock();
		}

		public void unlock() {
			this.nodeLock.unlock();
		}
	}

	private Node<E> head;

	public OptimisticList() {
		head = new Node<E>(Integer.MIN_VALUE);
		head.next = new Node<E>(Integer.MAX_VALUE);
	}

	// Nouveauté : validate, qui parcoure Liste jusqu'à pred, puis vérifie le chaînage en pred et curr;
	private boolean validate (Node<E> pred, Node<E> curr) {
		Node<E> valpred = head;
		while(valpred.key < pred.key) valpred = valpred.next;
		return (valpred.key == pred.key && pred.next == curr);
	}

	public boolean add(E item) {
		int key = item.hashCode();

		while (true) { // Boucle non-bornnée : Si la fonction n'a pas fait de return (*** a échoué parce qu'un autre thread a modifié le chaînage entre temps), il faut recommencer !
			Node<E> pred = head;
			Node<E> curr = head.next;
			while (curr.key < key) {
				pred = curr; curr = curr.next; 
			}

			pred.lock(); // * On prend le verrou sur pred au dernier moment, seulement lorsqu'on a trouvé le nœud intéressant
			try {
				curr.lock(); // ** Idem pour curr
				try {
					if (validate(pred, curr)) { // *** Vérifie que le chaînage logique entre pred été curr n'a pas été cassé entre * et **
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

	// Idem pour remove()
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

	// Idem pour contains()
	public boolean contains (E item) {
		int key = item.hashCode();

		while(true) {
			Node<E> pred = head;
			Node<E> curr = pred.next;
			while (curr.key<key) {
				pred = pred.next;
				curr = curr.next;
			}

			pred.lock();
			try {
				curr.lock();
				try{
					if (validate(pred, curr)) {
						return (curr.key == key);
					}
				}finally {
					curr.unlock();
				}
			}finally {
				pred.unlock();
			}
		}
	}
}