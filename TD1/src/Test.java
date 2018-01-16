import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;

import search.Manager;

public class Test {
    static final String datafile = "titles.txt";
    // static final String datafile = "titles.txt"; // For laptops, download local copy
    static LinkedBlockingQueue<String> result;
    static Map<String, Integer> count;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException
    {
        BufferedReader data = new BufferedReader(new FileReader(datafile));

        /**************************************************/
        /* Exercises 1 -- 5                               */
        /**************************************************/

        // result = Manager.search(data, "Asterix", 10);
        // Comment the next three lines for exercises 4 and 5
        // for (String res : result){
        //    System.out.println(res);
        // }

        // /**************************************************/
        // /* Exercise 6                                     */
        // /**************************************************/

        String[] queries = {"Asterix","Obelix"};
        count = Manager.count(data, queries);
        for (String res : count.keySet()){
            String msg = String.format("\"%s\" occurs %d time", res, count.get(res));
            System.out.println(msg);
        }
    }
}
