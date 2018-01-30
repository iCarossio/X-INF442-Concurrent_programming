public class Test {
	
	public static int test(Stack stack) {
        final int Ninserts = 500000;
        final int Nthreads = 10;
        
        Thread[] threads = new Thread[Nthreads];
        
        for (int p=0;p<Nthreads;p++) {
        		Tester t = new Tester(stack, Ninserts); // Tester est de classe Runnable
        		threads[p] = new Thread(t); // On encapsule le Runnable par un Thread
        		threads[p].start();
        }

        // On attent que tous les threads aient fini avant de continuer
        for (int p=0;p<Nthreads;p++) {
			try {
				threads[p].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
        }
        
        int len = 0;
        while (stack.pop() != null) {
        		len++;
        }
        
        return len;
	}
	
    public static void main(String args[]){
        Stack<Integer> stackSimple = new SafeStackSimple<>();
        Stack<Integer> stack = new SafeStack<>();
        Stack<Integer> stackSync = new SafeStack<>();
        
        long t0Simple = System.nanoTime();
        int resSimple = test(stackSimple);
        long t1Simple = System.nanoTime();
        long tSimple = t1Simple - t0Simple;
        
        long t0Sync = System.nanoTime();
        int resSync = test(stackSync);
        long t1Sync = System.nanoTime();
        long tSync = t1Sync - t0Sync;
        
        long t0 = System.nanoTime();
        int res = test(stack);
        long t1 = System.nanoTime();
        long t = t1-t0;
        
        System.out.println("StackSimple : "+ resSimple +" : "+tSimple+"s \n      Stack : "+ res +" : "+t+"s \n  StackSync : "+ resSync +" : "+tSync+"s");
        
        
    }
}