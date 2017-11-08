package Model.Graph.AirportGraph;


import Model.FileTools.FileFormat;
import Model.FileTools.FileManager;
import Model.FlightPriority;
import Model.Graph.AirportGraph.Structures.*;
import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.ArcInterface;
import Model.Graph.GraphStructures.Graph;
import Model.Graph.GraphStructures.Node;

import java.io.IOException;
import java.util.*;


public class AirportManager {

    private FlightGraph airportMap;
    List<Comparator<Arc<Airport,Flight>>> comparators;


    private Comparator<Arc<Airport,Flight>> cmpFlightDuration = new Comparator<Arc<Airport,Flight>>() {
        @Override
        public int compare(Arc<Airport,Flight> o1, Arc<Airport,Flight> o2) {
            return o1.getData().getFlightDuration() - o2.getData().getFlightDuration();
        }
    };

    private Comparator<Arc<Airport,Flight>> cmpPrecio = new Comparator<Arc<Airport,Flight>>() {
        @Override
        public int compare(Arc<Airport,Flight> o1, Arc<Airport,Flight> o2) {
            return (int) (o1.getData().getPrice() - o2.getData().getPrice());
        }
    };


    public AirportManager() {
        comparators = new ArrayList<>();
        comparators.add(cmpFlightDuration);
        comparators.add(cmpPrecio);



        airportMap = new FlightGraph(comparators);
    }

    public void addAirport(String airportName, double lat, double lng) {
        airportMap.addNode(new Airport(airportName, new Location(lat, lng)));

    }

    public void deleteAirport(String airportName){  airportMap.deleteNode(new Airport(airportName)); }

    public void deleteAirports() {
        airportMap.deleteGraph();
    }

    public void addFlight(String airline, String flightNumber, String[] days, String origin, String target,
                          int departureTime, int flightDuration, double price) {
        try {
            for (Day day: Day.getDays(days)) {
                Airport orig = new Airport(origin);
                Airport dest = new Airport(target);
                if(airportMap.contains(orig) && airportMap.contains(dest) ) {
                    Flight flight = new Flight(airline, flightNumber, day, departureTime, flightDuration, price);
                    airportMap.addArc(flight, orig, dest);

                }
            }

        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
    }

    public void deleteFlight(String airline, String flightNumber) {
        for (Day day: Day.getAllDays()) {
            airportMap.deleteArc(new Flight(airline, flightNumber));
        }
    }

    public void deleteFlights() {
        airportMap.deleteArcs();
    }

    public List<Airport> getAirports() {
        return (List<Airport>) airportMap.getNodesElements();
    }

    public Set<Flight> getFlights() {
        return airportMap.getArcsData();
    }

    public List<Arc<Airport,Flight>> findRoute(String origin, String dest, FlightPriority priority,
                                               List<Day> departureDays) {

        if(priority == FlightPriority.TOTAL_TIME) {
            return airportMap.minTotalTimePath(airportMap.getNodeElement(new Airport(origin)),
                    airportMap.getNodeElement(new Airport(dest)), comparators.get(0), departureDays);
        }
        else if(priority == FlightPriority.TIME){
            return airportMap.minPathFlightTime(airportMap.getNodeElement(new Airport(origin)),
                    airportMap.getNodeElement(new Airport(dest)), comparators.get(0),
                    departureDays);
        }else{
            return airportMap.minPathPrice(airportMap.getNodeElement(new Airport(origin)),
                    airportMap.getNodeElement(new Airport(dest)), comparators.get(1),
                    departureDays);
        }


    }
    public List<Arc<Airport, Flight>> world_trip(String origin, FlightPriority priority, List<Day> departureDays) {
        int index = priority.getValue();
        if (priority==FlightPriority.TOTAL_TIME) {
            return airportMap.world_trip(airportMap.getNodeElement(new Airport(origin)), comparators.get(0),
                    departureDays, priority);
        }else if (priority==FlightPriority.TIME){
            return airportMap.world_trip(airportMap.getNodeElement(new Airport(origin)), comparators.get(index),
                departureDays, priority);
        }
        return airportMap.world_trip(airportMap.getNodeElement(new Airport(origin)), comparators.get(index),
               departureDays, priority);
    }

    public Collection<Arc<Airport,Flight>> getAirportArcs(){
        return this.airportMap.getArcs();
    }


    public static void main(String[] args){
        AirportManager a = new AirportManager();
        a.addAirport("ARG", 0,0);
        a.addAirport("CHI", 1,2);
        a.addAirport("BRA", 2,0);
        a.addAirport("URU", 3,0);
        a.addAirport("PAR", 4,5);



        String[] days = new String[1];
        days[0] = "Lu";
        String[] tu = new String[2];
        tu[0] = "Ma";
        tu[1]  = "Lu";

        a.addFlight("san", "6",tu,"ARG","PAR",10*60,1*60,700 );

        a.addFlight("san", "1",tu,"ARG","BRA",7*60,7*60,400 );

        a.addFlight("san", "2",tu,"ARG","URU",9*60,2*60,900 );

        a.addFlight("san", "3",days,"BRA","URU",14*60,1*60,100 );

        a.addFlight("san", "4",days,"URU","BRA",14*60,1*60,100 );

        a.addFlight("san", "5",days,"ARG","CHI",20*60,13*60,1500 );

        a.addFlight("san", "7",tu,"PAR","CHI",13*60,3*60,300 );

        a.addFlight("san", "8",days,"CHI","ARG",10*60,2*60,1000 );

        a.addFlight("san", "9",days,"CHI","PAR",9*60,1*60,500 );

        a.addFlight("san", "10",days,"BRA","PAR",14*60,1*60,100 );

        a.addFlight("san", "11",days,"PAR","BRA",11*60,2*60,100 );

        a.addFlight("san", "12",days,"BRA","URU",15*60,1*60,100 );

        a.addFlight("san", "13",days,"URU","CHI",18*60,2*60,1900 );
        Airport from = new Airport("ARG");
        Airport to = new Airport("CHI");
        ArcInterface<Arc<Airport,Flight>> arcint = new ArcInterface<Arc<Airport,Flight>>(){
            public double convert(Arc<Airport,Flight> arc ){
                return (double)arc.getData().getFlightDuration();
            }
        };

        List<Day> ldays = new ArrayList<>();
        ldays.add(Day.getDay(0));
        ldays.add(Day.getDay(1));
        System.out.println("flight time");

        List<Arc<Airport,Flight>> route = a.findRoute("ARG", "CHI", FlightPriority.TIME, ldays);

        FileManager fm = new FileManager("src/test.txt", a);
        //System.out.println(route);
        fm.writeRoute(route,"stdout", FileFormat.TEXT);

        System.out.println("total time");
        route = a.findRoute("ARG", "CHI", FlightPriority.TOTAL_TIME, ldays);
        fm.writeRoute(route,"stdout", FileFormat.TEXT);
        //System.out.println(route);

        System.out.println("flight price");
        route = a.findRoute("ARG", "CHI", FlightPriority.PRICE, ldays);
        fm.writeRoute(route,"stdout", FileFormat.TEXT);
        //System.out.println(route);

        ArrayList<Day> wtdays = new ArrayList<>();
        wtdays.add(Day.getDay(1));
        wtdays.add(Day.getDay(2));
        //fm.writeRoute(a.world_trip("ARG", FlightPriority.PRICE, wtdays),"stdout",FileFormat.TEXT);
        //FileManager fm2 = new FileManager("src/test.txt", a);
   }

}
