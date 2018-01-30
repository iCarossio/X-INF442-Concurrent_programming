public class Tester implements Runnable {
    Stack<Integer> stack;
    int Ninserts;
    
    Tester(Stack<Integer> stack, int Ninserts){
    		this.stack = stack;
    		this.Ninserts = Ninserts;
    }

    public void run(){
        for (int i=0; i<Ninserts; i++) {
        		stack.push(i);
        }
    }
}