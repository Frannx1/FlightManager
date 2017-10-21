package Model.Graph;

import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.Node;

import java.util.HashSet;
import java.util.Set;

public class Graph<T,V> {

    private Set<Node<T,V>> nodes;
    private Set<Arc<T,V>> arcs;

    public Graph() {
        nodes = new HashSet<>();
        arcs = new HashSet<>();
    }

    public void addNode(T element){
        Node<T,V> node = new Node<>(element);
        if(!nodes.contains(node)){
            nodes.add(node);
        }
    }

    public void deleteNode(T element) {
        Node<T,V> node = new Node<>(element);
        if(nodes.contains(node)) {
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

    public void addFlight(Flight flight) throws Exception {
        if(!nodes.contains(flight.getOrigin()) || !nodes.contains(flight.getTarget())) {
            throw new Exception("The Airports of the flight were invalid.");        //ver q exception
        }
        if(!arcs.contains(flight)) {
            flight.getOrigin().addOutFlight(flight);
            flight.getTarget().addInFlight(flight);
            arcs.add(flight);
        }
    }

    public void deleteFlight(Flight flight) {
        if(arcs.contains(flight)) {
            flight.getTarget().removeInFlight(flight);
            flight.getOrigin().removeOutFlight(flight);
            arcs.remove(flight);
        }
    }

    public void deleteFlights() {
        for (Airport airport: nodes) {
            airport.clearFlights();
        }
    }
}
