package Model.Graph;

import Model.Graph.GraphStructures.Airport;
import Model.Graph.GraphStructures.Flight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AirportManager {

    private Map<String, Airport> airports;
    private Set<Flight> flights;

    public AirportManager() {
        airports = new HashMap<>();
        flights = new HashSet<>();
    }

    public void addAirport(Airport airport){
        if(!airports.containsKey(airport.getName())){
            airports.put(airport.getName(),airport);
        }
    }

    public void deleteAirport(Airport airport) {
        if(airports.containsKey(airport.getName())) {
            for (Airport aux: airport.getInAirports()) {
                aux.deleteFlightsTo(airport);
            }
            for (Flight flight: airport.getInFlights()) {
                flights.remove(flight);
            }
            for (Flight flight: airport.getOutFlights()) {
                flights.remove(flight);
            }
            airports.remove(airport.getName());
        }
    }

    public void deleteAiports() {
        airports.clear();
        flights.clear();
    }

    public void addFlight(Flight flight) throws Exception {
        if(!airports.containsKey(flight.getOrigin()) || !airports.containsKey(flight.getTarget())) {
            throw new Exception("The Airports of the flight were invalid.");        //ver q exception
        }
        if(!flights.contains(flight)) {
            flight.getOrigin().addOutFlight(flight);
            flight.getTarget().addInFlight(flight);
            flights.add(flight);
        }
    }

    public void deleteFlight(Flight flight) {
        if(flights.contains(flight)) {
            flight.getTarget().removeInFlight(flight);
            flight.getOrigin().removeOutFlight(flight);
        }
    }
}
