public class Synch extends Thread {
  int val;
  boolean condition = true;
 
   public void run() {
     if (condition) {
       while (condition) {
         val=val+1;
       }
     }
   }
 
   public static void main(String[] args) throws Exception {
     Synch s=new Synch();
     s.start();
     Thread.sleep(1000);
     s.condition=false;
     System.out.println(s.val);
     System.out.println(s.val);
     s.join();
     System.out.println("Done!");
   }
}

/* Since we put "s.condition=false;" This program should stop 
 * and print twice the same value an then "Done!" */

/* In fact, it doesn't stop, prints 2 different values, and no "Done!" */

/* In fact, the compiler optimizes the run function as if it were 
	public void run() {
	if (condition)
	  while (true) { <----- While TRUE (considering condition as a constant)
	      val=val+1;
	    }
	}
	
	To solve the problem, we have to declare condition as volatile :
	"volatile boolean condition = true;"

*/

