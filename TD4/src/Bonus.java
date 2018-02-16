public class Bonus {
  static int x;
  static int y;
  public static void main(String[] args) {
    try {
      for (int j = 0; j < 10; j++) {
        x = 0;
        y = 0;
 
        Thread t1 = new Thread() {
           public void run() {
             x = 1;
             for (int i = 0; i < 10000000; i++) {
               y = 1;
             }
           }
        };
 
        Thread t2 = new Thread() {
           public void run() {
              int ly = y;
              int lx = x;
              // Under SC, if we see y == 1 here, we should see x == 1.
              System.out.println("Thread 2: y = " + ly + ", x = " + lx);
           }
        };
 
        t1.start();
        t2.start();
        t1.join();
        t2.join();
      }
    } catch (InterruptedException e) { }
  }
}
