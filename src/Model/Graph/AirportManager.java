package Model.Graph;

import Model.Graph.GraphStructures.Airport;
import Model.Graph.GraphStructures.Flight;

import java.util.HashSet;
import java.util.Set;

public class AirportManager {

    private Set<Airport> airports;
    private Set<Flight> flights;

    public AirportManager() {
        airports = new HashSet<>();
        flights = new HashSet<>();
    }

    public void addAirport(Airport airport){
        if(!airports.contains(airport)){
            airports.add(airport);


        }
    }

    public void deleteAirport(Airport airport) {
        if(airports.contains(airport)) {
            for (Airport aux: airport.getInAirports()) {
                aux.deleteFlightsTo(airport);
            }
            for (Flight flight: airport.getInFlights()) {
                flights.remove(flight);
            }
            for (Flight flight: airport.getOutFlights()) {
                flights.remove(flight);
            }
            airports.remove(airport);
        }
    }

    public void deleteAiports() {
        airports.clear();
        flights.clear();
    }

    public void addFlight(Flight flight) throws Exception {
        if(!airports.contains(flight.getOrigin()) || !airports.contains(flight.getTarget())) {
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
            flights.remove(flight);
        }
    }

    public void deleteFlights() {
        for (Airport airport: airports) {
            airport.clearFlights();
        }
    }
}
