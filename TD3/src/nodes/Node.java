package nodes;

import java.util.Collection;
import java.util.LinkedList;

import data.Message;

public abstract class Node implements Runnable {
    private final Collection<Node> outgoingConnections;
    private final String nodeName;

    Node(String name) {
        this.nodeName = name;
        this.outgoingConnections = new LinkedList<>();
    }

    /**
     * A wrapper around <code>Thread.sleep()</code> that ignores
     * <code>InterruptedException</code>s.
     * 
     * @param millis
     *            Number of milliseconds to sleep
     */
    public static final void sleepUninterruptibly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Add a connection from <code>this</code> node to a different node.
     * 
     * @param dest
     *            The target node to add. May assume that it is non-null.
     */
    public void addConnectionTo(Node dest) {
        this.outgoingConnections.add(dest);
    }

    /**
     * Get the name of this node.
     */
    public String getName() {
        return this.nodeName;
    }

    /**
     * This method begins the execution of the node.
     */
    public void init() {
        new Thread(this).start();
    }

    /**
     * This queues a new message in the input for this node. May be called
     * concurrently with the thread(s) running the node.
     * 
     * @param msg
     *            The message to add to the queue. May assume that it is
     *            non-null.
     */
    abstract void putInQueue(Message msg);

    /**
     * Send a message to all outgoing connections.
     * 
     * @param msg
     *            The message to send to all destinations
     */
    final void forward(Message msg) {
        for (Node n : this.outgoingConnections)
            n.putInQueue(msg);
        Thread.yield();
    }
}
