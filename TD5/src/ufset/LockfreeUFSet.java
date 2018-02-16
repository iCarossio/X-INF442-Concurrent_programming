package ufset;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class LockfreeUFSet  implements UFSet{
  private final AtomicIntegerArray parents;
  private final int size;
  
  public LockfreeUFSet(int count) {
	this.size = count;
    this.parents = new AtomicIntegerArray(count);
    for (int i = 0; i < count; i++)
      this.parents.set(i, i);
  }
  
  public int getSize() {
	  return size;
  }

  public int find(int x) {
    int start = x;
    while (true) {
      int p = this.parents.get(x);
      if (x == p)
        break;
      x = p;
    }
    while (start != x)
      start = this.parents.getAndSet(start, x);
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
      return this.parents.compareAndSet(y, y, x);
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