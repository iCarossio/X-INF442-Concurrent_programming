package tests;
import ufset.*;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		int N=(int) (1.5*Runtime.getRuntime().availableProcessors());

		// reasonable values are size = N*1000 and Ntests > 40
		int size = N*1000;
		int Ntests = 40;
		
		Thread[] unionizers = new Thread[N];
		System.out.println("There are "+Ntests+" tests with:");
		System.out.println("Number of threads : "+N);
		System.out.println("Number of elements : "+size);
		System.out.println("If one of the tests takes more than a few seconds, the test failed "
				+ "and you should stop the execution manually.");
		
		for(int j=0;j<Ntests;j++) {
			// replace by the appropriate UFSet implementation
			UFSet u = new HalvingLockfreeUFSet(size);
			
			System.out.println("Starting test "+(j+1)+": ");
			long time = System.nanoTime();

			for (int i = 0; i < N; i++) {
				unionizers[i] = new Thread(new Unionizer(u, i));
				unionizers[i].start();
			}
			for (int i = 0; i < N; i++) {
				unionizers[i].join();
			}
			time = System.nanoTime() - time;
			System.out.println("	Successful after " + time / 1000000 + "ms");
		}
		System.out.println("All tests finished.");

	}
}
