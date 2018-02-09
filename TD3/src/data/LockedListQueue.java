package data;

import java.util.concurrent.locks.ReentrantLock;

import nodes.Node;

public class LockedListQueue implements MessageQueue{
	private static class Cell {
		public Message data;
		public Cell next;

		public Cell(Message data) {
			this.data = data;
			this.next = null;
		}
	}

	private Cell head, tail;
	private int length;
	ReentrantLock myLock = new ReentrantLock();

	public LockedListQueue() {
		Cell sentinel = new Cell(null);
		this.head = this.tail = sentinel;
		this.length = 0;
	}

	@Override
	public boolean add(Message msg) {
		myLock.lock();
		try {
			Cell newTail = new Cell(msg);
			this.tail.next = newTail;
			this.tail = newTail;
			this.length++;
			return true;
		} finally {
			myLock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		ReentrantLock myLock = new ReentrantLock();

		myLock.lock();
		try {
			return this.length == 0;
		} finally {
			myLock.unlock();
		}
	}

	@Override
	public Message remove() {
		while (true) {
			Cell next = this.head.next;
			if (null == next) {
				Node.sleepUninterruptibly(10);
				continue;
			}
			myLock.lock();
			try {
				this.head = next;
				Message ret = next.data;
				next.data = null;
				this.length--;
				return ret;
			} finally {
				myLock.unlock();
			}
		} 
	}
}
