package Model.Graph.AirportGraph;


import Model.Graph.AirportGraph.Structures.Airport;
import Model.Graph.AirportGraph.Structures.Day;
import Model.Graph.AirportGraph.Structures.Flight;
import Model.Graph.AirportGraph.Structures.Location;
import Model.Graph.GraphStructures.Graph;

import java.util.IllegalFormatException;


public class AirportManager {

    private Graph<Airport, Flight> airportMap;

    public AirportManager() {
        airportMap = new Graph<Airport, Flight>();
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
