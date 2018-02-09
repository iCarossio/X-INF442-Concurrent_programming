package data;

public interface MessageQueue {
    /**
     * Insert an item into the tail of the queue.
     * 
     * @returns true if and only if the item was successfully added
     */
    boolean add(Message msg);

    /**
     * @return true if and only if the queue is empty.
     */
    boolean isEmpty();

    /**
     * @return the element at the head of the queue after waiting (by blocking
     *         or polling) until the queue is non-empty.
     */
    Message remove();
}
