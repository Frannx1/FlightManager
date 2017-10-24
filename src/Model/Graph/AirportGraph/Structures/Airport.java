package Model.Graph.AirportGraph.Structures;

public class Airport {

    private String name;
    private Location location;

    public Airport(String name, Location location){
        this.name = name;
        this.location = location;
    }

    public Airport(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
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
    public int hashCode(){
        int hash = 1;
        hash = hash * 31 + ((name == null) ? 0 : name.hashCode());
        hash = hash * 29 + ((location == null) ? 0 : location.hashCode());
        return  hash;
    }
}
