package Model.Graph.GraphStructures;

import java.util.*;

public class Graph<T,V> {

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

    /**
     * This method will give you give you the path of minimum weight for the given nodes.
     * @param from the value of the node from which you want to start.
     * @param to the value of the node in  which you want to end.
     * @param arcInt Interface which determines how the data of an arc can ve interpreted as a weight
     * @return A list of all visited nodes that forms the path.
     */

    // pedimos una interface y no agregamos el metodo en arc porque vamos a querer el camino con respescto a
    // tres distintos aspectos, asi que no puedo tener en cuenta eso es un arc normal.

//    public List<Arc<T,V>> minPath(T from, T to, ArcInterface<Arc<T,V>> arcInt, Comparator<Arc<T,V>> cmp){
//        if(from == null || to == null){
//            throw new IllegalArgumentException("Bad input.");
//        }
//        clearMarks();
//        PriorityQueue<PQNode> pq = new PriorityQueue<>();
//
//        pq.offer(new PQNode(nodes.get(from), 0, null));
//
//        List<Arc<T,V>> path = new ArrayList<>();
//
//        while(!pq.isEmpty()){
//
//            PQNode<T,V> aux = pq.poll();
//            if(aux.node.getElement() == to){
//
//               return path;
//            }
//            if(!aux.node.getVisited()){
//                aux.node.setVisited(true);
//                path.add(aux.usedArc);
//                for(Node n : aux.node.getAdjacents()){
//                    Arc<T,V> r = (Arc) aux.node.getTree(n,cmp).first();
//                    if(!r.getTarget().getVisited())
//                        pq.offer(new PQNode(r.getTarget(),arcInt.convert(r) + aux.distance, r));
//                }
//            }
//        }
//        return  path;
//    }

    protected void clearMarks(){
        for (Node<T,V> n : nodes.values()){
            n.setVisited(false);
            n.setTag(0);

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
