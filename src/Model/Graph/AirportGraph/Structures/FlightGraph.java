package Model.Graph.AirportGraph.Structures;

import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.ArcInterface;
import Model.Graph.GraphStructures.Graph;
import Model.Graph.GraphStructures.Node;

import java.util.*;

public class FlightGraph extends Graph<Airport, Flight> {

    public FlightGraph(List<Comparator<Arc<Airport, Flight>>> comparators) {
        super(comparators);
    }


    public List<Arc<Airport, Flight>> minPath(Airport from, Airport to, ArcInterface<Arc<Airport, Flight>> arcInt,
                                              Comparator<Arc<Airport, Flight>> cmp, List<Day> days) {


        if (from == null || to == null) {
            throw new IllegalArgumentException("Bad input.");
        }

        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return null;
        }

        clearMarks();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();


        Node<Airport, Flight> origin = nodes.get(from);
        origin.setVisited(true);

        for (Node<Airport, Flight> n : origin.getAdjacents()) {
            Arc<Airport, Flight> r = origin.getTree(n, cmp).get(0);
            if (r.getData().departureOnDate(days)) {
                List<Arc<Airport, Flight>> path = new ArrayList<>();
                path.add(r);
                r.getData().setTagCurrentTime(r.getData().arrivalTime(0));
                pq.offer(new PQNode(n, arcInt.convert(r), path));
            }
        }

        if (pq.isEmpty()) {
            // no flight does match the requested departure day
            return null;
        }

        while (!pq.isEmpty()) {

            PQNode<Airport, Flight> aux = pq.poll();
            if (aux.node.getElement().equals(to)) {

                return aux.usedArcs;
            }
            if (!aux.node.getVisited()) {
                aux.node.setVisited(true);
                for (Node<Airport, Flight> n : (Set<Node<Airport, Flight>>) aux.node.getAdjacents()) {
                    Arc<Airport, Flight> r = (Arc<Airport, Flight>) aux.node.getTree(n, cmp).get(0);
                    if (!r.getTarget().getVisited())
                        aux.usedArcs.add(r);
                    r.getData().setTagCurrentTime(r.getData().arrivalTime(aux.usedArcs.get(aux.usedArcs.size() - 1).getData().getTagCurrentTime()));
                    pq.offer(new Graph.PQNode(r.getTarget(), (arcInt.convert(r) - aux.distance) + aux.distance, aux.usedArcs));
                }
            }
        }

        return null;

    }


    public List<Arc<Airport, Flight>> minTotalTimePath(Airport from, Airport to,
                                                       Comparator<Arc<Airport, Flight>> cmp, List<Day> days) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("Bad input.");
        }

        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return null;
        }

        clearMarks();
        PriorityQueue<PQTimeNode> pq = new PriorityQueue<>();


        Node<Airport, Flight> origin = nodes.get(from);
        origin.setVisited(true);

          Set<Node<Airport, Flight>> s = origin.getAdjacents();
        for (Node<Airport, Flight> n : s) {
            Arc<Airport, Flight> r = origin.getTree(n, cmp).get(0);
            if (r.getData().departureOnDate(days)) {
                List<Arc<Airport, Flight>> path = new ArrayList<>();
                path.add(r);
                Flight f = r.getData();
                r.getData().setTagCurrentTime(f.arrivalTime(0));// this gives me the current time of the week we flight started
                pq.offer(new PQTimeNode(n, r.getData().getFlightDuration(), path));
            }
        }


        while (!pq.isEmpty()) {

            PQTimeNode<Airport, Flight> aux = pq.poll();
            if (aux.node.getElement().equals(to)) {
                aux.usedArcs.get(aux.usedArcs.size() - 1).getData().setTagCurrentTime((int) aux.totalTime);
                return aux.usedArcs;
            }
            if (!aux.node.getVisited()) {
                aux.node.setVisited(true);
                for (Node<Airport, Flight> n : aux.node.getAdjacents()) {
                    // verify the node is indeed not visited
                    if (!n.getVisited()) {
                        Arc<Airport, Flight> bestFlightToNode = aux.node.getTree(n, cmp).get(0);
                        int bestArrivalTime = bestFlightToNode.getData().arrivalTime(aux.usedArcs.get(aux.usedArcs.size() - 1).getData().getTagCurrentTime());
                        int currentTime = aux.usedArcs.get(aux.usedArcs.size() - 1).getData().getTagCurrentTime();

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
                                addedTime = bestArrivalTime - currentTime;
                            }
                        }
                        List<Arc<Airport, Flight>>  usedArcs = new ArrayList<>();
                        usedArcs.addAll(aux.usedArcs);
                        usedArcs.add(bestFlightToNode);
                        pq.offer(new PQTimeNode(bestFlightToNode.getTarget(), addedTime, usedArcs));
                    }


                }
            }
        }
        return null;
    }

    protected class PQTimeNode<T, V> implements Comparable<PQTimeNode> {

        public Node<T, V> node;
        public double totalTime;
        public List<Arc<T, V>> usedArcs;

        public PQTimeNode(Node<T, V> n, double totaltime, List<Arc<T, V>> arcs) {
            this.node = n;
            this.totalTime = totaltime;
            usedArcs = arcs;
        }

        public int compareTo(PQTimeNode other) {
            return (int) (totalTime - other.totalTime);
        }
    }

    public static class World_tripComparator implements Comparator<Arc<Airport,Flight>>{
        private Comparator<Arc<Airport,Flight>> arcComparator;
        World_tripComparator(Comparator<Arc<Airport,Flight>>cmp){
            this.arcComparator=cmp;
        }

        @Override
        public int compare(Arc<Airport, Flight> o1, Arc<Airport, Flight> o2) {
            int cmp1= (o1.getTarget().getVisited())?1:0;
            int cmp2=(o2.getTarget().getVisited())?1:0;
            if (cmp1>cmp2) return 1;
            if (cmp1<cmp2){
                return -1;
            }
            return this.arcComparator.compare(o1,o2);

        }
    }

/**
     *
     * @param from Is the Airport you want to take your World_Trip
     * @param cmp  The comparator you use.
     * @param days The Days you want to start yout worldTrip
     * @param priority the priority of the WorldTripSearch
     * @return A List of Arcs if there is a WorldTrip. Else NULL;
     *
     */
    public List<Arc<Airport, Flight>> world_trip(Airport from, Comparator<Arc<Airport, Flight>> cmp, List<Day> days, FlightPriority priority) {
        if (from == null ) {
            throw new IllegalArgumentException("Bad input.");
        }
        if (!(nodes.containsKey(from))) {
            return null;
        }
        clearMarks();
        clearArcMarks();
        Solution solution=new Solution();
        if (priority==FlightPriority.TIME){
            solution=world_Trip_ft(from,from,solution,this.nodes.size(),new World_tripComparator(cmp),days);
        }
        if (priority==FlightPriority.PRICE) {
            solution=world_Trip_pr(from,from,solution,this.nodes.size(),new World_tripComparator(cmp),days);
        }
        if (priority==FlightPriority.TOTAL_TIME){
            solution=world_Trip_tt(from,from,solution,this.nodes.size(),new World_tripComparator(cmp),days);
        }
        if (solution.hasSolution()) {
            return solution.toList();
        }
        return null;
    }

    private Solution world_Trip_pr(Airport from, Airport current, Solution solution, int n,
                                           Comparator<Arc<Airport,Flight>>cmp, List<Day> days){

        Node<Airport,Flight> curr=nodes.get(current);
        Node<Airport,Flight> origin=nodes.get(from);

        if (n==0 && curr.equals(origin)){
            solution.updateSolution();
            return solution;
        }

        PriorityQueue<Arc<Airport,Flight>> pq = new PriorityQueue<>(cmp);
        curr.setVisited(true);

        if (curr.equals(origin) && n==this.nodes.size()){
            for (Node<Airport, Flight> t : origin.getAdjacents()) {
                Arc<Airport, Flight> r = origin.getTree(t, cmp).get(0);
                if (r.getData().departureOnDate(days)) {
                    pq.offer(r);
                }
            }

            if (pq.isEmpty()){
                //No flights does match the requested departure day
                return solution;
            }
            while (!pq.isEmpty()){
                Arc<Airport,Flight> aux=pq.poll();
                aux.setVisited(true);
                solution.addFlight(aux,aux.getData().getPrice().doubleValue());
                solution=world_Trip_pr(from,aux.getTarget().getElement(),solution,n-1,cmp,days);
                solution.removeFlight();
                aux.setVisited(false);
            }
            return solution;
        }

        for (Arc<Airport,Flight> m: curr.getOutArcs()){
            pq.offer(m);
        }
        while(!pq.isEmpty()){
            Arc<Airport,Flight> aux=pq.poll();
            if (!aux.isVisited()) {
                aux.setVisited(true);
                solution.addFlight(aux, (aux.getData().getPrice()).doubleValue());
                if (solution.betterThanBest()) {
                    if (!aux.getTarget().getVisited()) {
                        solution = world_Trip_pr(from, aux.getTarget().getElement(), solution, n - 1, cmp,days);
                    } else {
                        solution = world_Trip_pr(from, aux.getTarget().getElement(), solution, n, cmp,days);
                    }
                }
                solution.removeFlight();
                aux.setVisited(false);
            }
        }
        curr.setVisited(false);
        return solution;
    }

    private Solution world_Trip_ft(Airport from, Airport current, Solution solution, int n,
                                           Comparator<Arc<Airport,Flight>>cmp, List<Day> days) {

        Node<Airport,Flight> curr=nodes.get(current);
        Node<Airport,Flight> origin=nodes.get(from);

        if (n==0 && curr.equals(origin)){
            solution.updateSolution();
            return solution;
        }

        PriorityQueue<Arc<Airport,Flight>> pq = new PriorityQueue<>(cmp);
        curr.setVisited(true);

        if (curr.equals(origin) && n==this.nodes.size()){
            for (Node<Airport, Flight> t : origin.getAdjacents()) {
                Arc<Airport, Flight> r = origin.getTree(t, cmp).get(0);
                if (r.getData().departureOnDate(days)) {
                    pq.offer(r);
                }
            }

            if (pq.isEmpty()){
                //No flights does match the requested departure day
                return solution;
            }
            while (!pq.isEmpty()){
                Arc<Airport,Flight> aux=pq.poll();
                aux.setVisited(true);
                solution.addFlight(aux,aux.getData().getFlightDuration().doubleValue());
                solution=world_Trip_ft(from,aux.getTarget().getElement(),solution,n-1,cmp,days);
                solution.removeFlight();
                aux.setVisited(false);
            }
            return solution;
        }

        for (Arc<Airport,Flight> m: curr.getOutArcs()){
            pq.offer(m);
        }
        while(!pq.isEmpty()){
            Arc<Airport,Flight> aux=pq.poll();
            if (!aux.isVisited()) {
                aux.setVisited(true);
                solution.addFlight(aux, (aux.getData().getFlightDuration()).doubleValue());
                if (solution.betterThanBest()) {
                    if (!aux.getTarget().getVisited()) {
                        solution = world_Trip_ft(from, aux.getTarget().getElement(), solution, n - 1, cmp,days);
                    } else solution = world_Trip_ft(from, aux.getTarget().getElement(), solution, n, cmp, days);
                }
                solution.removeFlight();
                aux.setVisited(false);
            }
        }
        curr.setVisited(false);
        return solution;
    }
    private Solution world_Trip_tt(Airport from, Airport current, Solution solution, int n,
                                           Comparator<Arc<Airport,Flight>>cmp, List<Day> days) {
        Node<Airport, Flight> curr = nodes.get(current);
        Node<Airport, Flight> origin = nodes.get(from);
        PriorityQueue<Arc<Airport, Flight>> pq = new PriorityQueue<>(cmp);

        if (n == 0 && curr.equals(origin)) {
            solution.updateSolution();
            return solution;
        }

        curr.setVisited(true);

        if (curr.equals(origin) && n == this.nodes.size()) {
            for (Node<Airport, Flight> t : origin.getAdjacents()) {
                Arc<Airport, Flight> r = origin.getTree(t, cmp).get(0);
                if (r.getData().departureOnDate(days)) {
                    pq.offer(r);
                    r.getData().setTagCurrentTime(r.getData().arrivalTime(0));

                }
            }

            if (pq.isEmpty()) {
                //No flights does match the requested departure day
                return solution;
            }
            while (!pq.isEmpty()) {
                Arc<Airport, Flight> aux = pq.poll();
                aux.setVisited(true);
                solution.addFlight(aux, aux.getData().getFlightDuration().doubleValue());
                solution = world_Trip_tt(from, aux.getTarget().getElement(), solution, n - 1, cmp, days);
                solution.removeFlight();
                aux.setVisited(false);
            }
            return solution;
        }

        for (Arc<Airport, Flight> m : curr.getOutArcs()) {
            pq.offer(m);
        }
        while (!pq.isEmpty()) {
            Arc<Airport, Flight> aux = pq.poll();
            if (!aux.isVisited()) {
                aux.setVisited(true);
                int ArrivalTime=aux.getData().arrivalTime(solution.peekArc().getData().getTagCurrentTime());
                aux.getData().setTagCurrentTime(ArrivalTime);
                solution.addFlight(aux, (double)ArrivalTime);
                if (solution.betterThanBest()) {
                    if (!aux.getTarget().getVisited()) {
                        solution = world_Trip_tt(from, aux.getTarget().getElement(), solution, n - 1, cmp, days);
                    } else {
                        solution = world_Trip_tt(from, aux.getTarget().getElement(), solution, n, cmp, days);
                    }
                }
                solution.removeFlight();
                aux.setVisited(false);
            }
        }
        curr.setVisited(false);
        return solution;
    }

    private static class Solution{
        private boolean solution;
        private Deque<Arc<Airport, Flight>> currentTrip;
        private double currentCost;
        private Deque cost;
        private Deque<Arc<Airport, Flight>> bestTrip;
        private double bestCost;

        Solution(){
            this.solution=false;
            this.currentCost=0;
            this.bestCost=0;
            this.currentTrip=new LinkedList<>();
            this.bestTrip=new LinkedList<>();
            this.cost=new LinkedList<>();
        }

        public boolean betterThanBest(){
            if (!hasSolution()){
                return true;
            }
            return (currentCost> bestCost);
        }

        public boolean hasSolution(){
            return solution;
        }

        public double peekCost(){
            return (double)cost.peek();
        }

        public Arc<Airport, Flight> peekArc(){
            return currentTrip.peek();
        }

        public void addFlight(Arc<Airport,Flight> flight, double cost){
            this.cost.push(cost);
            currentCost=currentCost+cost;
            currentTrip.push(flight);
        }

        public void removeFlight(){
            currentCost = (double)((int)currentCost - (int)cost.pop());
            currentTrip.pop();
            return;
        }

        public void updateSolution(){
            if (betterThanBest()){
                this.solution=true;
                bestCost=currentCost;
                bestTrip=currentTrip;
            }
            return;
        }

        public List<Arc<Airport,Flight>> toList(){
            Deque<Arc<Airport,Flight>> aux=new LinkedList<>();
            while(!bestTrip.isEmpty()){
                aux.push(bestTrip.pop());
            }
            List<Arc<Airport,Flight>> solutionTrip=new LinkedList<>();
            while(!aux.isEmpty()){
                Arc<Airport,Flight> ArcAux=aux.pop();
                bestTrip.push(ArcAux);
                solutionTrip.add(ArcAux);
            }
            return solutionTrip;
        }
    }

}
