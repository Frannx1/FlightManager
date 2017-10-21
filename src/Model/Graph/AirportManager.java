package Model.Graph;

import Model.Graph.GraphStructures.Airport;
import Model.Graph.GraphStructures.Flight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportManager {

    private Map<String, Airport> airports;

    public AirportManager() {
        airports = new HashMap<>();
    }

    public void addAirport(Airport airport){
        if(!airports.containsKey(airport.getName())){
            airports.put(airport.getName(),airport);
        }
    }

    public void deleteAirport(String airportName) {
        if(airports.containsKey(airportName)) {
            Airport aux = airports.get(airportName);
            for (Airport airport: aux.getInAirports()) {
                airport.deleteFlightsTo(aux);
            }
            airports.remove(airportName);
        }
    }

    public void deleteAiports() {
        airports.clear();
    }

    public void addFlight(Flight flight) throws Exception {
        if(!airports.containsKey(flight.getOrigin()) || !airports.containsKey(flight.getTarget())) {
            throw new Exception("The Airports of the flight were invalid.");        //ver q exception
        }
    }

    public void deleteFlights() {

    }
}
