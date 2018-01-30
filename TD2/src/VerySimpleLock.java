import java.util.concurrent.atomic.AtomicBoolean;

public class VerySimpleLock{
    AtomicBoolean locked = new AtomicBoolean(false);

    /* performing all the lock operations atomically 
     * is critical for guaranteeing mutual exclusion 
     * Sinon : au moment où on ferait un .unlock(), alors >=2 threads 
     * pourraient avoir la valeur while (lock == true) à true 
     * en même temps et ainsi locker en même temps myLock();
     */

    public void lock(){
        while (!locked.compareAndSet(false, true));
    }
    public void unlock(){
    		locked.set(false);
    }
}