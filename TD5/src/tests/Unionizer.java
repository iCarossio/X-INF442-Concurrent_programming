package tests;

import ufset.UFSet;

class Unionizer implements Runnable {
	private UFSet u;
	private int n;
	private int size;
	
	public Unionizer(UFSet u, int n) {
		this.u = u;
		this.n = n;
		this.size = u.getSize();
	}
	
	public void defaultTest() throws InterruptedException {
		for(int i=0;i<(size/10)-1;i++) {
			switch (n%4) {
			case 0: 
				u.find(10*i);
				break;
			case 1: 
				u.union(10*i+9,10*(i+1)+9);
				break;
			case 2:
				u.find(10*(i+1));
				break;
			default:
				u.find(10*i+4);
				break;
			}
		}
		trivialTest();
	}
	public void run() {
		try {
			defaultTest();
		} catch (InterruptedException e) {};
		
	}

	
	public void trivialTest() {
		for (int i=0;i<size-1;i++) {
		    if(n%2==0){
				u.union(i, i+1);
			} else {
				u.union(size-2-i,size-1-i);
			}
				
		}
		
	}
}
