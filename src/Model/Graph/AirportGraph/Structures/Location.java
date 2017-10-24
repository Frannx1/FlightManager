package Model.Graph.AirportGraph.Structures;


public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !getClass().equals(o.getClass())){
            return false;
        }

        Location aux = (Location) o;

        return getLatitude() == aux.getLatitude() && getLongitude() == aux.getLongitude();

    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash + 31 * (int) latitude;
        hash = hash + 23 * (int) longitude;
        return  hash;
    }

    @Override
    public String toString(){
        return "(Lat:"+latitude+" Long:"+longitude+").";
    }
}
