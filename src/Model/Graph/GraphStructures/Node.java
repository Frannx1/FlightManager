package Model.Graph.GraphStructures;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node<T,V> {

    private T element;
    private List<Arc<T,V>> outArcs;
    private List<Arc<T,V>> inArcs;
    private boolean visited;
    private int tag;

    public Node(T element) {
        this.element = element;
        this.inArcs = new ArrayList<>();
        this.outArcs = new ArrayList<>();
        this.visited = false;
        this.tag = 0;
    }

    public List<Arc<T,V>> getInArc(){
        return inArcs;
    }

    public List<Arc<T,V>> getOutArc(){
        return outArcs;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public int getTag(){
        return  this.tag;
    }

    public T getElement(){
        return  this.element;
    }

    public void addInArc(Arc<T,V> arc) {
        inArcs.add(arc);
    }

    public void removeInArc(Arc<T,V> arc) {
        inArcs.remove(arc);
    }

    public void addOutArc(Arc<T,V> arc) {
        outArcs.add(arc);
    }

    public void removeOutArc(Arc<T,V> arc) {
        outArcs.remove(arc);
    }

    public void clearArcs() {
        inArcs.clear();
        outArcs.clear();
    }

    public List<Node<T,V>> getInElements() {
        List<Node<T,V>> elements = new ArrayList<>();
        for (Arc<T,V> arc: inArcs) {
            elements.add(arc.getOrigin());
        }
        return elements;
    }

    public void deleteArcsTo(Node<T,V> node) {
        outArcs.removeIf(tvArc -> tvArc.getTarget().equals(node));
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;
        if(!getClass().equals(obj.getClass())) return false;
        Node<T,V> node = (Node) obj;
        return this.element.equals(node.getElement());
    }

    @Override
    public int hashCode(){
        return this.element.hashCode();
    }

    @Override
    public String toString(){
        return this.element.toString();
    }
}
