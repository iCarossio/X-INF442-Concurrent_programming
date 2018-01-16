package search;

import java.util.concurrent.LinkedBlockingQueue;

public class Printer extends Thread {
    LinkedBlockingQueue<String> in;
    int items;
    int num;

    public Printer(LinkedBlockingQueue<String> in, int num) {
    		this.in    = in;
    		this.items = 0;
    		this.num   = num;
    	}

    public void printStatus() {
        System.out.format("Thread \"%s\" processed %d items.\n",
                          this.getName(), this.items);
    }

    public void run() {
    		try {
			String s = in.take();
			while (!s.equals(Searcher.EOF) && items < num) {
				System.out.println(s);
				items++;
				s = in.take();
			}
		} 
    		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
