import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockfreeList<E> {

	public class Node<E> {

		final private E item;
		final private int key;
		private volatile AtomicMarkableReference<Node<E>> next; // Le champ Next est un AtomicMarkableReference
		// Plus de lock, plus de boolén marked

		public Node(E obj, Node<E> next) {
			this.item = obj;
			this.key = item.hashCode();
			this.next = new AtomicMarkableReference<Node<E>>(next, false); // Initialisation correcte 
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
	
	// Nouveauté : utilisée pour modéliser une paire (pred, curr)
	class Window {
		public Node<E> pred, curr;
		Window (Node<E> myPred, Node<E> myCurr) {
			pred = myPred;
			curr = myCurr; 
		} 
	}

	private Node<E> head;

	public LockfreeList() {
		head = new Node<E>(Integer.MIN_VALUE);
		head.next = new AtomicMarkableReference<Node<E>>(new Node<E>(Integer.MAX_VALUE), false); // Initialisation correcte 
	}


	// Cherche la paire de nœuds sur laquelle on va effectuer l'opération add() ou remove()
	public Window find(Node<E> head, int key) {
		Node<E> pred = null, curr = null, succ = null;
		boolean [] marked = { false};
		boolean snip;

		retry : while(true) { // * Le retry est un marqueur en lien avec **
			pred = head;
			curr = pred.next.getReference();

			while (true) {
				succ = curr.next.get(marked); // Récupère le marquage du nœud courrant curr lors du parcours
	
				// Efface (physiquement), si possible, les éléments marqués à true (et donc supprimés logiquement) lors du parcours
				while (marked [0]) {
					snip = pred.next.compareAndSet(curr, succ, false, false);
					if (!snip) continue retry; // ** Si on n'as pas réussi à effacer les nœuds, on recommence à retry
					// Si l'effacement a fonctionné et qu'on n'est pas encore allé assez loin dans la liste, on continue le parcours
					curr = succ;
					succ = curr.next.get(marked); 
				}
				
				if (curr.key >= key) // Dans ce cas, on a trouvé le bon couple !
					return new Window(pred, curr);
				pred = curr; curr = succ; 
			} 
		} 
	}

	
	public boolean add(E item) {
		int key = item.hashCode();

		// Boucle non bornée : le code est répété autant de fois que nécessaire
		while (true) {
			Window window = find(head, key); // Cherche la paire de nœuds entre lesquels on va insérer le nouvel objet
			Node<E> pred = window.pred;
			Node<E> curr = window.curr;
			
			if (curr.key == key) {
				return false;
			} else {
				// Construit le nœud à insérer, pointant sur le nœud curr trouvé par find, en le marquant présent
				Node<E> node = new Node<E> (item);
				node.next = new AtomicMarkableReference<Node<E>>(curr, false);
				
				// Si pred.next == curr (1, curr) et que le chaînage n'a pas été modifié (3, false) 
				// ==> Alors pred.next = node (2, node) avec et fait désormais partie de la liste (4, false)
				if (pred.next.compareAndSet (curr, node, false, false)) { 
					return true; 
				} 
			}

		} 
	}

	public boolean remove(E item) {
		int key = item.hashCode();
		boolean snip;

		while (true) {
			Window window = find(head, key); // Idem que add()
			Node<E> pred = window.pred, curr = window.curr;
			
			// Si le nœud correspondant à ce qu'on veut effacer n'est pas là --> faux
			if (curr.key != key) {
				return false; 
			} else { // Sinon on essaie de marquer le nœud trouvé à true, pour l'efface logiquement
				Node<E> succ = curr.next.getReference();
				snip = curr.next.attemptMark (succ, true);
				if (!snip)  // Si cela échoue, on recommence la boucle while
					continue; 
				pred.next.compareAndSet(curr, succ, false, false); // Si tout va bien, on enlève physiquement le nœud marqué
				return true; 
			} 
		} 
	}

	
	public boolean contains(E item) {
		boolean[] marked = {false};
		int key = item.hashCode();
		Node<E> curr = head;

		while (curr.key < key) {
			curr = curr.next.get(marked); // Récupère la référence dans curr et son marqueur dans marked
		}

		return (curr.key == key && !marked[0]); // Fait attention au fait que le nœud éventuellement trouvé est effacé logiquement
	}

}
