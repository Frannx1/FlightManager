package Model.Graph.GraphStructures;


public class Location {

    private double latitude;
    private double longitude;

    private int timeZone;

    public Location(double latitude, double longitude, int timeZone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZone = timeZone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTimeZone() {
        return timeZone;
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
        hash = hash + 29 * timeZone;
        return  hash;
    }

    @Override
    public String toString(){
        return "(Lat:"+latitude+" Long:"+longitude+").";
    }
}
