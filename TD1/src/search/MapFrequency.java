package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class MapFrequency extends Thread {
    private BufferedReader data;
    private String[] queries;
    private LinkedBlockingQueue<String> result;
    private int processedItems;
    private int matchesFound;

    public MapFrequency(BufferedReader data, String[] queries, LinkedBlockingQueue<String> result) {
      this.data    = data;
      this.queries = queries;
      this.result  = result;
    }

    public void printStatus() {
        System.out.format("Thread \"%s\" processed %d items and found %d matching results.\n",
                          this.getName(), this.processedItems, this.matchesFound);
    }

    @Override
    public void run() {
		try {
	        String s = data.readLine();
	        while (s != null) {
	        		for (String query:queries) {
		    	        if (s.indexOf(query) >= 0) {
		    	        		result.add(query);
		    	        		matchesFound++;
		    	        }
	        		}
	    	        processedItems++;
	    	        if (Thread.interrupted()==true) return;
	    	        s = data.readLine();
	        }
	        result.add(Searcher.EOF);
		}
		catch (IOException e){
			result.add(Searcher.EOF);
		}
    }
}