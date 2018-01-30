public class Stack<E> {
	
	private Node top;

    private class Node{
        final E data;
        final Node next;
        Node (E data, Node next){
        		this.data = data;
        		this.next = next;
        }
    }

    public E pop(){
    		if (top == null) return null;
    		E data = top.data;
        top = top.next;
        return data;
    }

    public void push(E el){
        top = new Node(el, top);
    }
}