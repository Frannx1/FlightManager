package Model.Graph.GraphStructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Airport {

    private Location location;
    private String name;
    private List<Flight> outFlights;
    private List<Flight> inFlights;
    private boolean visited;
    private int tag;

    public Airport(Location location, String name) {
        if(location == null) throw  new IllegalArgumentException("Null Location.");
        if(name == null) throw  new IllegalArgumentException("Null name.");
        this.location = location;
        this.name = name;
        this.inFlights = new ArrayList<>();
        this.outFlights = new ArrayList<>();
        this.visited = false;
        this.tag = 0;

    }

    public Location getLocation(){
        return  this.location;
    }

    public String getName(){
        return this.name;
    }

    public List<Flight> getInFlights(){
        return inFlights;
    }

    public List<Flight> getOutFlights(){
        return outFlights;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public int getTag(){
        return  this.tag;
    }

    public void addInFlight(Flight flight) {
        inFlights.add(flight);
    }

    public void addOutFlight(Flight flight) {
        outFlights.add(flight);
    }

    public List<Airport> getInAirports() {
        List<Airport> airports = new ArrayList<>();
        for (Flight flight: inFlights) {
            airports.add(flight.getOrigin());
        }
        return airports;
    }

    public void deleteFlightsTo(Airport airport) {
        Iterator<Flight> flightIterator = outFlights.iterator();
        while (flightIterator.hasNext()) {
            if(flightIterator.next().getTarget().equals(airport)) {
                flightIterator.remove();
            }
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;
        if (getClass() != obj.getClass()) return false;
        Airport aux = (Airport) obj;
        return name.equals(aux.getName());
    }


    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + ((name == null) ? 0 : name.hashCode());
        hash = hash * 31 + ((location == null) ?  0 : location.hashCode());
        return  hash;
    }

    @Override
    public String toString(){
        return  name + location.toString();
    }

}
