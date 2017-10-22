package Model.Graph.AirportGraph;


import Model.Graph.GraphStructures.Graph;

import java.util.IllegalFormatException;


public class AirportManager {


    private Graph<Airport, Flight> airportMap;


    public AirportManager(){
        airportMap = new Graph<>();
    }


    public void addAirport(Airport a){
        airportMap.addNode(a);
    }

    public void addFlight( Flight f){
        try {
            airportMap.addArc(f,f.getOrigin(), f.getDestination());
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
    }

    public void deleteAirport(Airport a){
        airportMap.deleteNode(a);
    }

    public void deleteFlight(Flight f){
        airportMap.deleteArc(f);
    }

    public void deleteFlight(int flightId, String airline){
        Flight aux = new Flight(flightId, airline);
        airportMap.deleteArc(aux);
    }


}
