class Dekker {
  public Dekker () {
    flag[0] = false; flag[1] = false; turn = 0;
  }
 
  public void Pmutex(int t) {
    int other;
 
    other = 1-t;
    flag[t] = true;
    while (flag[other] == true) { // Si l'autre thread utilise le Counter
      if (turn == other) { // Si c'est vraiment au tour de l'autre thread
        
    	  	/* Je n'utilise pas le counter tant que ce n'est pas à mon tour */
    	  	flag[t] = false;
        while (turn == other)
          ;
        flag[t] = true;
      }
    }
  }
 
  public void Vmutex(int t) {
    turn = 1-t;
    flag[t] = false;
  }
 
  volatile private int turn;
  private boolean [] flag = new boolean [2];
}
