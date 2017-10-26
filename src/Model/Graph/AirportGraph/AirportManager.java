package Model.Graph.AirportGraph;


import Model.Graph.AirportGraph.Structures.*;
import Model.Graph.GraphStructures.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;


public class AirportManager {

    private Graph<Airport, Flight> airportMap;

    private Comparator<Flight> cmpFlightDuration = new Comparator<Flight>() {
        @Override
        public int compare(Flight o1, Flight o2) {
            return o1.getFlightDuration() - o2.getFlightDuration();
        }
    };

    private Comparator<Flight> cmpPrecio = new Comparator<Flight>() {
        @Override
        public int compare(Flight o1, Flight o2) {
            return (int) (o1.getPrice() - o2.getPrice());
        }
    };

    public AirportManager() {
        List<Comparator<Flight>> comparators = new ArrayList<>();
        comparators.add(cmpFlightDuration);
        comparators.add(cmpPrecio);
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
            Flight flight = new Flight(airline, flightNumber, Day.getDays(days), departureTime, flightDuration, price);

            airportMap.addArc(flight, airportMap.getNodeElement(new Airport(origin)),
                                airportMap.getNodeElement(new Airport(target)));

        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
    }

    public void deleteFlight(String airline, String flightNumber) {
        airportMap.deleteArc(new Flight(airline, flightNumber));
    }

    public void deleteFlights() {
        airportMap.deleteArcs();
    }

}
