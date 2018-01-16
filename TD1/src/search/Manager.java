package search;
import java.io.BufferedReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*;

public class Manager {
    public static LinkedBlockingQueue<String> simpleSearch(BufferedReader data, String query, int num)
        throws InterruptedException{
        LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
        Searcher s = new Searcher(data, query, result);
        s.setName("searcher");
        s.start();
        Thread.sleep(100);
        s.printStatus();
        return result;
    }
    
    public static LinkedBlockingQueue<String> pollingSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            s.setName("searcher");
            s.start();
            
            while (s.isAlive() == true) {
	            Thread.sleep(100);
	            s.printStatus();
            }
            return result;
    }

    public static LinkedBlockingQueue<String> waitingSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            s.setName("searcher");
            s.start();
            s.join();
            s.printStatus();
            return result;
    }
    
    public static LinkedBlockingQueue<String> pipelinedSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            s.setName("searcher");
            s.start();

            Printer p = new Printer(result, 10);
            p.setName("printer");
            p.start();
            
            p.join();
            s.join();
            
            s.printStatus();
            p.printStatus();
            return result;
    }
    
    public static LinkedBlockingQueue<String> interruptingSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            s.setName("searcher");
            s.start();

            Printer p = new Printer(result, num);
            p.setName("printer");
            p.start();
            
            p.join();
            s.interrupt();
            s.join();
            
            s.printStatus();
            p.printStatus();
            return result;
    }

    public static LinkedBlockingQueue<String> concurrentSearch(BufferedReader data, String query, int num)
            throws InterruptedException{
            LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
            Searcher s = new Searcher(data, query, result);
            Searcher t = new Searcher(data, query, result);
            s.setName("searcher1");
            t.setName("searcher2");
            s.start();
            t.start();

            Printer p = new Printer(s.result, num);
            p.setName("printer");
            p.start();
            
            p.join();
            s.interrupt();
            t.interrupt();
            s.join();
            t.join();
            
            s.printStatus();
            t.printStatus();
            p.printStatus();
            return result;
    }

    public static LinkedBlockingQueue<String> search(BufferedReader data, String query, int num){
        try {
            return concurrentSearch(data, query, num);
        } catch (InterruptedException e) {
            System.out.println("Search interrupted.");
            throw new RuntimeException("Unexpected search interruption");
        }
    }

    public static Map<String,Integer> count(BufferedReader data, String[] queries)
        throws InterruptedException{
        LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();
        MapFrequency s = new MapFrequency(data, queries, result);
        s.setName("MapFrequency");;
        s.start();

        ReduceFrequency p = new ReduceFrequency(result);
        p.setName("ReduceFrequency");
        p.start();

        p.join();
        s.join();
        
        s.printStatus();
        p.printStatus();
        return p.count;

    }
}
