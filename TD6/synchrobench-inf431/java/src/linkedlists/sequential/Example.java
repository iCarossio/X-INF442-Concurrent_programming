package linkedlists.sequential;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;

import contention.abstractions.CompositionalIterator;
import contention.abstractions.CompositionalSortedSet;

public class SetList {

	private class Node {
		Object item;
		int key;
		Node next;
	}

	private Node head;

	public SetList() {
		head = new Node (Integer.MIN_VALUE) ;
		head.next = new Node (Integer.MAX_VALUE) ;
	}
	
	
}
