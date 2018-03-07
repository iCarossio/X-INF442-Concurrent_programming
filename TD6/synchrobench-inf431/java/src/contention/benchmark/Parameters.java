package contention.benchmark;

/**
 * Parameters of the Java version of the 
 * Synchrobench benchmark.
 *
 * @author Vincent Gramoli
 */
public class Parameters {
    
    public static int numThreads = 16, 
    	numMilliseconds = 3000,
    	numWrites = 40,
    	numWriteAlls = 0,
    	numSnapshots = 0,
    	size = 1024,
    	range = 2*size,
	warmUp = 5,
    	iterations = 1;
    
    public static boolean detailedStats = true;

    //public static String benchClassName = new String("linkedlists.lockbased.LockedLinkedListSortedSet");
    //public static String benchClassName = new String("linkedlists.lockbased.LockCouplingListSortedSet");
    public static String benchClassName = new String("linkedlists.lockbased.OptimisticListSortedSet");
    //public static String benchClassName = new String("linkedlists.lockbased.LazyListSortedSet");
    //public static String benchClassName = new String("linkedlists.lockbased.VersionedListSet");
    //public static String benchClassName = new String("linkedlists.lockfree.NonBlockingLinkedListSet");
}
