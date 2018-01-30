import java.util.concurrent.locks.ReentrantLock;

public class SafeStack<E> extends Stack<E> {
	
	ReentrantLock myLock = new ReentrantLock();
	
    public void push(E el) {
    		myLock.lock();
    		try {
    			  super.push(el);
    		} finally {
    			  myLock.unlock();
    		}
    }

}