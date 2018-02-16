package ufset;

public interface UFSet {
	/**
	 * Returns the total number of elements in the structure.
	 */
	public int  getSize();
	
	/**
	 * Function joining two equivalence classes.
	 */
	public void union(int x, int y);
	/**
	 * Function testing whether two elements are in the same
	 * equivalence class.
	 */
	public boolean isSame(int i, int j);
	
	/**
	 * Find the representative of x
	 */
	public int find(int x);
	
}
