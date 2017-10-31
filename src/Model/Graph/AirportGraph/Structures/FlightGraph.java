package Model.Graph.AirportGraph.Structures;

import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.ArcInterface;
import Model.Graph.GraphStructures.Graph;
import Model.Graph.GraphStructures.Node;

import java.util.*;

public class FlightGraph extends Graph<Airport, Flight> {

    public FlightGraph(List<Comparator<Arc<Airport,Flight>>> comparators) {
        super(comparators);
    }


    public List<Arc<Airport,Flight>> minPath(Airport from, Airport to, ArcInterface<Arc<Airport,Flight>> arcInt,
                                             Comparator<Arc<Airport,Flight>> cmp, List<Day> days ){


        if(from == null || to == null){
            throw new IllegalArgumentException("Bad input.");
        }

        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return null;
        }

        clearMarks();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();


        Node<Airport, Flight> origin = nodes.get(from);
        origin.setVisited(true);

        for(Node<Airport, Flight> n : origin.getAdjacents()){
            Arc<Airport,Flight> r = origin.getTree(n, cmp).first();
            if(r.getData().departureOnDate(days)) {
                List<Arc<Airport,Flight>> path = new ArrayList<>();
                path.add(r);
                r.getData().setTagCurrentTime(r.getData().arrivalTime(0));
                pq.offer(new PQNode(n, arcInt.convert(r), path));
            }
        }

        if(pq.isEmpty()){
            // no flight does match the requested departure day
            return null;
        }

        while(!pq.isEmpty()){

            PQNode<Airport,Flight> aux =  pq.poll();
            if(aux.node.getElement().equals(to)){

                return aux.usedArcs;
            }
            if(!aux.node.getVisited()){
                aux.node.setVisited(true);
                for(Node<Airport, Flight> n : (Set<Node<Airport, Flight>>) aux.node.getAdjacents()){
                    Arc<Airport,Flight> r = (Arc<Airport, Flight>) aux.node.getTree(n, cmp).first();
                    if(!r.getTarget().getVisited())
                        aux.usedArcs.add(r);
                        r.getData().setTagCurrentTime(r.getData().arrivalTime(aux.usedArcs.get(aux.usedArcs.size()-1).getData().getTagCurrentTime()));
                        pq.offer(new Graph.PQNode(r.getTarget(),(arcInt.convert(r)-aux.distance) + aux.distance, aux.usedArcs));
                }
            }
        }

        return  null;

    }





    public List<Arc<Airport,Flight>> minTotalTimePath(Airport from, Airport to,
                                                      Comparator<Arc<Airport,Flight>> cmp, List<Day> days){

        if(from == null || to == null){
            throw new IllegalArgumentException("Bad input.");
        }

        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return null;
        }

        clearMarks();
        PriorityQueue<PQTimeNode> pq = new PriorityQueue<>();


        Node<Airport, Flight> origin = nodes.get(from);
        origin.setVisited(true);

        for(Node<Airport, Flight> n : origin.getAdjacents()){
            Arc<Airport,Flight> r = origin.getTree(n, cmp).first();
            if(r.getData().departureOnDate(days)) {
                List<Arc<Airport,Flight>> path = new ArrayList<>();
                path.add(r);
                Flight f = r.getData();
                r.getData().setTagCurrentTime(f.arrivalTime(0) );// this gives me the current time of the week we flight started
                pq.offer(new PQTimeNode(n, r.getData().getFlightDuration() , path));
            }
        }



        while(!pq.isEmpty()){

            PQTimeNode<Airport,Flight> aux =  pq.poll();
            if(aux.node.getElement().equals(to)){
                aux.usedArcs.get(aux.usedArcs.size()-1).getData().setTagCurrentTime((int) aux.totalTime);
                return aux.usedArcs;
            }
            if(!aux.node.getVisited()) {
                aux.node.setVisited(true);
                for (Node<Airport, Flight> n : aux.node.getAdjacents()) {
                    // verify the node is indeed not visited
                    if(!n.getVisited()) {
                        Arc<Airport,Flight> bestFlightToNode = aux.node.getTree(n, cmp).first();
                        int bestArrivalTime = bestFlightToNode.getData().arrivalTime(aux.usedArcs.get(aux.usedArcs.size()-1).getData().getTagCurrentTime());
                        int currentTime = aux.usedArcs.get(aux.usedArcs.size()-1).getData().getTagCurrentTime();

                        int addedTime = bestArrivalTime - currentTime;
                        // we search for the best time in regard to the arrival time
                        for (Arc<Airport, Flight> r : aux.node.getTree(n, cmp)) {
                            // aux.distance is the current time of the week at which the algorithm expects to be
                            // en criollo , es el momento de la semana en que estas cuando llegas a este nodo

                            int currentArrivalTime = r.getData().arrivalTime(currentTime);

                            if (currentArrivalTime < bestArrivalTime) {
                                r.getData().setTagCurrentTime(currentArrivalTime);
                                bestArrivalTime = currentArrivalTime;
                                bestFlightToNode = r;
                                addedTime = bestArrivalTime  - currentTime;
                            }
                        }
                        aux.usedArcs.add(bestFlightToNode);
                        pq.offer(new PQTimeNode(bestFlightToNode.getTarget(), addedTime , aux.usedArcs));
                    }


                }
            }
        }
        return  null;
    }

    protected class PQTimeNode<T,V> implements Comparable<PQTimeNode>{

        public Node<T,V> node;
        public double totalTime;
        public List<Arc<T,V>> usedArcs;

        public PQTimeNode(Node<T,V> n, double totaltime, List<Arc<T,V>> arcs){
            this.node = n;
            this.totalTime = totaltime;
            usedArcs = arcs;
        }

        public int compareTo(PQTimeNode other){
            return (int)  (totalTime - other.totalTime);
        }
    }
/**
    World_ Trip in progress...
    Armando busqueda de ciclos Hamiltoniano

    public List<Arc<Airport,Flight>> world_trip(Airport from, ArcInterface<Arc<Airport,Flight>> arcInt,
                                                Comparator<Arc<Airport,Flight>> cmp, List<Day> days ){
        if(from == null || to == null){
            throw new IllegalArgumentException("Bad input.");
        }

        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return null;
        }

        clearMarks();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();


        Node<Airport, Flight> origin = nodes.get(from);
        origin.setVisited(true);

        for(Node<Airport, Flight> n : origin.getAdjacents()){
            Arc<Airport,Flight> r = origin.getTree(n, cmp).first();
            if(r.getData().departureOnDate(days)) {
                List<Arc<Airport,Flight>> path = new ArrayList<>();
                path.add(r);
                pq.offer(new PQNode(n, arcInt.convert(r), path));
            }
        }

        if(pq.isEmpty()){
            // no flight does match the requested departure day
            return null;
        }

        while(!pq.isEmpty()){

            PQNode<Airport,Flight> aux =  pq.poll();
            if(aux.node.getElement().equals(to)){

                return aux.usedArcs;
            }
            if(!aux.node.getVisited()){
                aux.node.setVisited(true);
                for(Node<Airport, Flight> n : (Set<Node<Airport, Flight>>) aux.node.getAdjacents()){
                    Arc<Airport,Flight> r = (Arc<Airport, Flight>) aux.node.getTree(n, cmp).first();
                    if(!r.getTarget().getVisited())
                        aux.usedArcs.add(r);
                    pq.offer(new Graph.PQNode(r.getTarget(),(arcInt.convert(r)-aux.distance) + aux.distance, aux.usedArcs));
                }
            }
        }

        return  null;

    }

    private static class Solution<Flight>{
        private boolean solution;
        Deque<Flight> currentTrip;
        double CurrentCost;
        Deque<Flight> bestTrip;
        double bestCost;

        Solution(){
            this.solution=false;
            this.currentTrip=0;
            this.bestTrip=0;
            this.currentTrip=new ArrayList();
            this.bestTrip=new ArrayList();
        }

        public boolean betterThanBest(){
            if (!hasSolution()){
                return true;
            }
            return (currentTrip> bestTrip);
        }

        public boolean hasSolution(){
            return solution;
        }

        public void addFlight(Flight flight){
            currentTrip.push(flight);
        }

        public void removeTrip(){
            currentTrip.pop();
            return;
        }
    }
**/


}
