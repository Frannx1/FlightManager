package Model.Graph.GraphStructures;

import java.util.*;

public class Graph<T,V> {

    private Map<T,Node<T,V>> nodes;
    private Map<V,Arc<T,V>> arcs;

    public Graph() {
        nodes = new HashMap<>();
        arcs = new HashMap<>();
    }

    public void addNode(T element){
        if(!nodes.containsKey(element)){
            Node<T,V> node = new Node<>(element);
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


    /**
     * This method will give you give you the path of minimum weight for the given nodes.
     * @param from the value of the node from which you want to start.
     * @param to the value of the node in  which you want to end.
     * @param arcInt Interface which determines how the data of an arc can ve interpreted as a weight
     * @return A list of all visited nodes that forms the path.
     */

    // pedimos una interface y no agregamos el metodo en arc porque vamos a querer el camino con respescto a
    // tres distintos aspectos, asi que no puedo tener en cuenta eso es un arc normal.

    public List<Node<T,V>> minPath(T from, T to, ArcInterface<Arc<T,V>> arcInt ){
        clearMarks();

        if(from == null || to == null){
            throw new IllegalArgumentException("Bad input.");
        }
        clearMarks();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();

        pq.offer(new PQNode(nodes.get(from), 0));

        List<Node<T,V>> path = new ArrayList<>();

        while(!pq.isEmpty()){
            PQNode<T,V> aux = pq.poll();
            if(aux.node.getElement() == to){
                path.add(nodes.get(to));
               return path;
            }
            if(!aux.node.getVisited()){
                aux.node.setVisited(true);
                path.add(aux.node);// porque siempre el que saco de la PQ es el mejor
                for(Arc r : aux.node.getOutArcs()){
                    // el if es una mejora
                    if(!r.getTarget().getVisited())
                        pq.offer(new PQNode(r.getTarget(),arcInt.convert(r) + aux.distance ));
                }
            }
        }

        return  path;

    }

    private void clearMarks(){
        for (Node<T,V> n : nodes.values()){
            n.setVisited(false);
            n.setTag(0);
        }
    }

    private class PQNode<T,V> implements Comparable<PQNode>{

        Node<T,V> node;
        double distance;

        public PQNode(Node<T,V> n, double distance){
            this.node = n;
            this.distance = distance;
        }

        public int compareTo(PQNode other){
            return Double.valueOf(distance).compareTo(other.distance);
        }
    }
}
