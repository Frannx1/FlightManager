package Model;

import java.util.ArrayList;
import java.util.List;

public class Airport {

    private Location location;
    private String name;
    private List<Flight> neighbours;
    private boolean visited;
    private int tag;

    public Airport(Location location, String name) {
        if(location == null) throw  new IllegalArgumentException("Null Location.");
        if(name == null) throw  new IllegalArgumentException("Null name.");
        this.location = location;
        this.name = name;
        this.neighbours = new ArrayList<>();
        this.visited = false;
        this.tag = 0;

    }

    public Location getLocation(){
        return  this.location;
    }

    public String getName(){
        return this.name;
    }

    public List<Flight> getNeighbours(){
        return  neighbours;
    }

    public boolean getVisited(){
        return this.visited;
    }

    public int getTag(){
        return  this.tag;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;
        if (getClass() != obj.getClass()) return false;
        Airport aux = (Airport) obj;
        if(location.equals(aux.getLocation()) && name.equals(aux.getName())){
            if(neighbours.equals(aux.getNeighbours()) && visited == aux.getVisited())
                return tag == aux.getTag();
        }
        return false;
    }

    @Override
    public String toString(){
        return  name + location.toString();
    }

}
