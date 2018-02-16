class UnsafeCounter {
    private int count;
    void increment () { count ++; }
    int get () { return count; }
}
 
class DekkerCounterTest {
    final static int STEPS = 5000000;
 
    static int i = 0;
 
    static UnsafeCounter c = new UnsafeCounter ();
 
    static Dekker m = new Dekker();
 
    public static void main (String[] args) throws InterruptedException {
      System.out.println("Dekker Counter Demo");
 
      // Spawn THREADS threads...
      Thread[] thread = new Thread [2];
      for (i = 0; i < 2; i++) {
        thread[i] = new Thread (new Runnable () {
          int tid = i;
          public void run () {
            System.out.println("Running: "+tid);
            // ...each thread increments the counter STEP times
            for (int s = 0; s < STEPS; s++) {
              m.Pmutex(tid);
              c.increment();
              m.Vmutex(tid);
            }
          }
        });
        thread[i].start();
      }
 
      // Wait for the threads to terminate
      for (int i = 0; i < 2; i++) thread[i].join();
        // Print the result.
        System.out.println("The final value of the counter is " + c.get() + ".");
    }
}
