public class SafeStackSimple<E> extends Stack<E> {
	
	VerySimpleLock myLock = new VerySimpleLock();
	
    public void push(E el) {
    		myLock.lock();
    		try {
    			  super.push(el);
    		} finally {
    			  myLock.unlock();
    		}
    }

}
