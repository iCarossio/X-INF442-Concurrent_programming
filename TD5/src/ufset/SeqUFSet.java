package ufset;
/**
 * Rankless union-find with path compression.
 */
public class SeqUFSet implements UFSet {
  /**
   * The elements are numbered 0..parent.length - 1. Each element points to the
   * canonical element of its equivalence class.
   */
  private final int[] parent;
  public final int size;

  /**
   * Returns the total number of elements.
   */
  public int getSize() {
	  return size;
  }
  
  public SeqUFSet(int count) {
	this.size = count;	
    this.parent = new int[count];
    
    // Initially, all elements point to themselves, meaning they are all in
    // singleton equivalence classes.
    for (int i = 0; i < count; i++) {
      this.parent[i] = i;
    }
  }

  /**
   * Find the canonical representative of the equivalence class of a given
   * element by going up in the 'parent' relationship.
   * 
   * Once this canonical representative is found, reset all the parent points on
   * the path from the argument to it.
   */
  public int find(int x) {
    int start = x;
    while (x != this.parent[x])
      x = this.parent[x];
    while (this.parent[start] != x) {
      int y = this.parent[start];
      this.parent[start] = x;
      start = y;
    }
    return x;
  }

  /**
   * Are the canonical representatives of the two given elements the same, i.e.,
   * are they in the same equivalence class?
   */
  public boolean isSame(int i, int j) {
    return this.find(i) == this.find(j);
  }

  /**
   * Assuming the arguments are both canonical representatives, make the first 
   * link to the other.
   */
  private void link(int x, int y) {
      this.parent[x] = y;
  }

  /**
   * Put the arguments in the same equivalence class by linking their canonical
   * representatives.
   */
  public void union(int x, int y) {
    x = this.find(x);
    y = this.find(y);
    this.link(x, y);
  }

}
