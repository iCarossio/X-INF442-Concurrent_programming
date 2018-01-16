package search;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class ReduceFrequency extends Thread {
    private LinkedBlockingQueue<String> in;
    public Map<String,Integer> count;
    private int items;

    public ReduceFrequency(LinkedBlockingQueue<String> in) {
      this.in = in;
      this.count = new HashMap<String,Integer>();
    }

    public void printStatus() {
        System.out.format("Thread \"%s\" processed %d items.\n",
                          this.getName(), this.items);
    }
    @Override
    public void run() {
		try {
			String s = in.take();
			while (!s.equals(Searcher.EOF)) {
				if (!count.containsKey(s)) count.put(s, 1);
				else count.put(s,count.get(s)+1);
				s = in.take();
				items++;
			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
