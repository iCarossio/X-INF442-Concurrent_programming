package ufset;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class HalvingLockfreeUFSet  implements UFSet{
  private final AtomicIntegerArray parents;
  private final AtomicIntegerArray rank;
  private final int size;
  
  public HalvingLockfreeUFSet(int count) {
	this.size = count;
    this.parents = new AtomicIntegerArray(count);
    this.rank = new AtomicIntegerArray(count);
    for (int i = 0; i < count; i++) {
      this.parents.set(i, i);
      this.rank.set(i, 0);
    }
    	
  }
  
  public int getSize() {
	  return size;
  }

  public int find(int x) {
    while (true) {
      int p = this.parents.get(x);
      if (x == p)
        break;
      this.parents.compareAndSet(x, p, this.parents.get(p));
      x = p;
    }
    return x;
  }

  public boolean isSame(int x, int y) {
    while (true) {
      x = this.find(x);
      y = this.find(y);
      if (x == y)
        return true;
      if (this.parents.get(x) == x)
        return false;
    }
  }

  private boolean link(int x, int y) {
	  if(x<y) return this.parents.compareAndSet(x, x, y);
	  else return this.parents.compareAndSet(y,y,x);
  }

  public void union(int x, int y) {
    do {
      do {
        x = this.find(x);
        y = this.find(y);
        if (x == y)
          return;
      } while (!(x == this.parents.get(x) && y == this.parents.get(y)));
    } while (!this.link(x, y));
  }
}