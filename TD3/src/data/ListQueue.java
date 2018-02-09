package data;

import nodes.Node;

public class ListQueue implements MessageQueue {
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

    public ListQueue() {
        Cell sentinel = new Cell(null);
        this.head = this.tail = sentinel;
        this.length = 0;
    }

    @Override
    public boolean add(Message msg) {
        Cell newTail = new Cell(msg);
        this.tail.next = newTail;
        this.tail = newTail;
        this.length++;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public Message remove() {
        while (true) {
            Cell next = this.head.next;
            if (null == next) {
                Node.sleepUninterruptibly(10);
                continue;
            }
            this.head = next;
            Message ret = next.data;
            next.data = null;
            this.length--;
            return ret;
        }
    }
}
