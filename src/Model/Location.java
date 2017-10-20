package Model;


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
    public String toString(){
        return "(Lat:"+latitude+" Long:"+longitude+").";
    }
}
