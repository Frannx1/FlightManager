package Model;



import java.util.Comparator;
import java.util.Iterator;


public class SortedList<T> implements Iterable<T> {

    private Node head;
    private Comparator<T> cmp;

    private int size;
    public SortedList(Comparator<T> cmp){
        this.cmp = cmp;
        head = null;
    }


    public void add(T element){
        if(head == null) {
            head = new Node(element);
        }else{
            head = add(head, element);
        }
    }

    public Node add(Node n, T element){

        if(n == null || cmp.compare(n.element, element) >= 0){
            Node aux =  new Node(element);
            aux.next = n;
            size++;
            return aux;
        }

        n.next = add(n.next, element);
        return n;
    }


    public void remove(T element){
        head = remove(head, element);

    }


    private Node remove(Node n, T element){

        if(n == null || cmp.compare(n.element, element) >  0){
            return n;
        }

        if(cmp.compare(n.element, element) == 0){
            size--;
            return remove(n.next, element);
        }

        n.next = remove(n.next, element);
        return n;
    }


    public boolean isEmpty(){
        return size == 0;
    }


    public int size(){
        return size;
    }
    public  boolean contains(T element){
        return contains(head, element);
    }

    private boolean contains(Node n, T element){

        if(n == null || cmp.compare(n.element, element) < 0){
            return false;
        }

        if(cmp.compare(n.element, element) == 0){
            return true;
        }

        return contains(n.next, element);
    }

    public T get(int index){
        if(index >= 0 || index < size){
            return get(head, index);
        }
        return null;
    }


    private T get(Node n, int index){

        if(index == 0){
            return n.element;
        }

        return get(n.next, index-1);
    }


    public Iterator<T> iterator(){
        return new Iterator<T>() {
            Node current;
            {current = head;}

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T result = current.element;
                current = current.next;
                return result;
            }
        };
    }

    private class Node{
        public T element;
        public Node next;

        public Node(T element){
            this.element = element;
        }

    }


}
