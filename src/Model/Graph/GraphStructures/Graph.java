package Model.Graph.GraphStructures;

import java.util.*;

public class  Graph<T,V> {

    protected Map<T,Node<T,V>> nodes;
    protected Map<V,Arc<T,V>> arcs;
    protected List<Comparator<Arc<T,V>>> comparators;

    public Graph(List<Comparator<Arc<T,V>>> comparators) {
        nodes = new HashMap<>();
        arcs = new HashMap<>();
        this.comparators = new ArrayList<>();

        this.comparators = comparators;
    }

    public void addNode(T element){
        if(!nodes.containsKey(element)){
            Node<T,V> node = new Node<>(element, comparators);
            nodes.put(element, node);
        }
    }

    public void deleteNode(T element) {
        if(nodes.containsKey(element)) {
            Node<T,V> node = nodes.get(element);
            for (Node<T,V> aux: node.getInElements()) {
                aux.deleteArcsTo(node);
            }
            for (Arc<T,V> arc: node.getInArcs()) {
                arcs.remove(arc);
            }
            for (Arc<T,V> arc: node.getOutArcs()) {
                arcs.remove(arc);
            }
            nodes.remove(node);
        }
    }

    public void deleteGraph() {
        nodes.clear();
        arcs.clear();
    }

    public void addArc(V data, T org, T dest) throws IllegalFormatException {
        if(!nodes.containsKey(org) || !nodes.containsKey(dest)) {
            throw new IllegalArgumentException("The origin or target node were invalid.");
        }
        if(!arcs.containsKey(data)) {
            Arc<T,V> arc = new Arc<>(data, nodes.get(org), nodes.get(dest));
            arcs.put(data, arc);
        }
    }

    public void deleteArc(V data) {
        if(arcs.containsKey(data)) {
            Arc<T,V> arc = arcs.get(data);
            arc.getTarget().removeInArc(arc);
            arc.getOrigin().removeOutArc(arc);
            arcs.remove(arc);
        }
    }

    public void deleteArcs() {
        for (Node<T,V> node: nodes.values()) {
            node.clearArcs();
        }
    }

    public T getNodeElement(T element) {
        return nodes.get(element).getElement();
    }

    public V getArcData(V data) {
        return arcs.get(data).getData();
    }

    public Comparator<Arc<T,V>> getComparator(int index){
        return comparators.get(index);
    }

    public Set<T> getNodesElements() {
        return nodes.keySet();
    }

    public Set<V> getArcsData() {
        return arcs.keySet();
    }


    public Collection<Arc<T,V>> getArcs(){
        return arcs.values();
    }

    protected void clearMarks(){
        for (Node<T,V> n : nodes.values()){
            n.setVisited(false);
            n.setTag(0);

        }
    }
    
    protected void clearArcMarks(){
        for (Arc<T,V> a:arcs.values()){
            a.setVisited(false);
        }
    }

    protected class PQNode<T,V> implements Comparable<PQNode>{

        public Node<T,V> node;
        public double distance;
        public List<Arc<T,V>> usedArcs;

        public PQNode(Node<T,V> n, double distance, List<Arc<T,V>> arcs){
            this.node = n;
            this.distance = distance;
            usedArcs = arcs;
        }

        public int compareTo(PQNode other){
            return (int) (distance - other.distance);
        }
    }
}
