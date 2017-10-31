package Model.Graph.AirportGraph;


import Model.Graph.AirportGraph.Structures.*;
import Model.Graph.GraphStructures.Arc;
import Model.Graph.GraphStructures.ArcInterface;
import Model.Graph.GraphStructures.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;


public class AirportManager {

    private FlightGraph airportMap;

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

    private Comparator<Arc<Airport, Flight>> cmpTotalFlightTime = new Comparator<Arc<Airport, Flight>>() {
        @Override
        public int compare(Arc<Airport, Flight> o1, Arc<Airport, Flight> o2) {
            return o1.getData().arrivalTime(0) - o2.getData().arrivalTime(0);
        }
    };

    public AirportManager() {
        List<Comparator<Arc<Airport,Flight>>> comparators = new ArrayList<>();
        comparators.add(cmpFlightDuration);
        comparators.add(cmpPrecio);
        comparators.add(cmpTotalFlightTime);
        airportMap = new FlightGraph(comparators);
    }

    public void addAirport(String airportName, double lat, double lng) {
        airportMap.addNode(new Airport(airportName, new Location(lat, lng)));
    }

    public void deleteAirport(String airportName) {
        airportMap.deleteNode(new Airport(airportName));
    }

    public void deleteAirports() {
        airportMap.deleteGraph();
    }

    public void addFlight(String airline, String flightNumber, String[] days, String origin, String target,
                          int departureTime, int flightDuration, double price) {
        try {
            for (Day day: Day.getDays(days)) {
                Flight flight = new Flight(airline, flightNumber, day, departureTime, flightDuration, price);
                airportMap.addArc(flight, airportMap.getNodeElement(new Airport(origin)),
                        airportMap.getNodeElement(new Airport(target)));
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


    public List<Arc<Airport,Flight>> findPath(String origin, String dest, Comparator<Arc<Airport,Flight>> cmp,
                         ArcInterface<Arc<Airport,Flight>> arcInt, List<Day>departureDays){

        List<Arc<Airport,Flight>> result =  airportMap.minPath(airportMap.getNodeElement(new Airport(origin)), airportMap.getNodeElement(new Airport(dest))
        , arcInt, cmp, departureDays);
        return result;
    }



    public static void main(String[] args){
        AirportManager a = new AirportManager();
        a.addAirport("ARG", 0,0);
        a.addAirport("CHI", 1,0);
        a.addAirport("BRA", 2,0);
        a.addAirport("URU", 3,0);
        a.addAirport("PAR", 4,0);



        String[] days = new String[1];
        days[0] = "Lu";
        String[] tu = new String[2];
        tu[0] = "Ma";
        tu[1]  = "Lu";

        a.addFlight("san", "1",days,"ARG","BRA",7*60,7*60,400 );

        a.addFlight("san", "2",days,"ARG","URU",9*60,2*60,900 );

        a.addFlight("san", "3",days,"BRA","URU",14*60,1*60,100 );

        a.addFlight("san", "4",days,"BRA","URU",14*60,1*60,100 );

        a.addFlight("san", "5",days,"ARG","CHI",20*60,13*60,1500 );

        a.addFlight("san", "6",tu,"ARG","PAR",10*60,1*60,700 );

        a.addFlight("san", "7",days,"PAR","CHI",13*60,3*60,300 );

        a.addFlight("san", "8",days,"CHI","ARG",10*60,2*60,1000 );

        a.addFlight("san", "9",days,"CHI","PAR",9*60,1*60,500 );

        a.addFlight("san", "10",days,"BRA","URU",14*60,1*60,100 );

        a.addFlight("san", "10",tu,"PAR","BRA",11*60,2*60,100 );

        Airport from = new Airport("ARG",new Location(0,0));
        Airport to = new Airport("BRA",new Location(3,0));
        ArcInterface<Arc<Airport,Flight>> arcint = new ArcInterface<Arc<Airport,Flight>>(){
            public double convert(Arc<Airport,Flight> arc ){
                return (double)arc.getData().getFlightDuration();
            }
        };

        List<Day> ldays = new ArrayList<>();
        ldays.add(Day.getDay(0));
        ldays.add(Day.getDay(1));

        List<Arc<Airport,Flight>> route = a.airportMap.minTotalTimePath(from, to ,a.cmpFlightDuration, ldays);

        for(Arc<Airport,Flight> r : route){
            System.out.println("Origin: "+ r.getOrigin() + " dest: "+ r.getTarget() + " "+ r.getData());
        }

        System.out.println("Total time: " + route.get(route.size()-1).getData().getTagCurrentTime()/60 + "hs" );


        System.out.println("done");

    }

}
