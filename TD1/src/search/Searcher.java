package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class Searcher extends Thread {
    public static final String EOF = "--EOF--";
    private final BufferedReader data;
    private final String query;
    final LinkedBlockingQueue<String> result;
    private int processedItems = 0;
    private int matchesFound = 0;

    public Searcher(BufferedReader data,
                    String query,
                    LinkedBlockingQueue<String> result){
        this.data = data;
        this.query = query;
        this.result = result;
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
	    	        if (s.indexOf(query) >= 0) {
	    	        		result.add(s);
	    	        		matchesFound++;
	    	        }
	    	        processedItems++;
	    	        if (Thread.interrupted()==true) return;
	    	        s = data.readLine();
    	        }
    	        result.add(EOF);
    		}
    		catch (IOException e){
    			result.add(EOF);
    		}
    }

}
