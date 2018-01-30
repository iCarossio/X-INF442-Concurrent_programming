public class SyncStackSimple<E> extends Stack<E> {
    synchronized public void push(E el) {
    		super.push(el);
    }
}


