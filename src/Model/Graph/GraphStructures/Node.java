package Model.Graph.GraphStructures;


import java.util.*;

public class Node<T,V> {

    private T element;
    private Map<Node<T,V>, List<TreeSet<Arc<T,V>>>> outArcs;
    private List<Arc<T,V>> inArcs;
    private List<Comparator<Arc<T,V>>> comparators;
    private boolean visited;
    private int tag;

    public Node(T element, List<Comparator<Arc<T,V>>> comparators) {
        this.element = element;
        this.inArcs = new ArrayList<>();
        this.outArcs = new HashMap<>();
        this.comparators = comparators;
        this.visited = false;
        this.tag = 0;
    }

    public List<Arc<T,V>> getInArcs(){
        return inArcs;
    }

    public List<Arc<T, V>> getOutArcs(){
        List<Arc<T,V>> list = new ArrayList<>();
        for (List<TreeSet<Arc<T, V>>> nodeList: outArcs.values()) {
            list.addAll(nodeList.get(0));
        }
        return list;
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
        if(!outArcs.containsKey(arc.getTarget())) {
            List<TreeSet<Arc<T,V>>> list = new ArrayList<>();
            outArcs.put(arc.getTarget(), list);

            for (Comparator<Arc<T,V>> cmp: comparators) {
                list.add(new TreeSet<Arc<T, V>>(cmp));
            }
        }
        List<TreeSet<Arc<T,V>>> nodeArcs = outArcs.get(arc.getTarget());
        for (TreeSet<Arc<T,V>> set: nodeArcs) {
            set.add(arc);
        }
    }

    public void removeOutArc(Arc<T,V> arc) {
        if(outArcs.containsKey(arc.getTarget())) {
            List<TreeSet<Arc<T,V>>> nodeArcs = outArcs.get(arc.getTarget());
            for (TreeSet<Arc<T,V>> set: nodeArcs) {
                set.remove(arc);
            }
        }
    }

    public TreeSet<Arc<T,V>> getTree(Node<T,V> node, Comparator<Arc<T,V>> cmp) {
        return outArcs.get(node).get(comparators.indexOf(cmp));
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
        outArcs.remove(node);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;
        if(!getClass().equals(obj.getClass())) return false;
        Node node = (Node) obj;
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
