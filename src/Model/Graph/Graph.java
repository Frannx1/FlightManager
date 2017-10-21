package Model.Graph;

import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.Node;

import java.util.HashMap;
import java.util.Map;


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

    public void deleteAiports() {
        nodes.clear();
        arcs.clear();
    }

    public void addArc(V data, T org, T dest) throws Exception {
        if(!nodes.containsKey(org) || !nodes.containsKey(dest)) {
            throw new Exception("The origin or target node were invalid.");        //ver q exception
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
}
