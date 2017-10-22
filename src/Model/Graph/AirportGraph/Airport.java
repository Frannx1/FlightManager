package Model.Graph.AirportGraph;



public class Airport {

    private String name;
    private Location location;

    public Airport(String name, Location location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || !obj.getClass().equals(this.getClass())){
            return false;
        }

        Airport aux = (Airport) obj;

        return aux.getName().equals(name) && aux.getLocation().equals(location);
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash * 31 + ((name == null) ? 0 : name.hashCode());
        hash = hash * 29 + ((location == null) ? 0 : location.hashCode());
        return  hash;
    }
}
