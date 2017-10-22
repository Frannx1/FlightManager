package Model.Graph.AirportGraph;

import java.sql.Time;

public  class Flight {

    private double cost;
    private double duration;
    private int ID;
    private Time flydeparture;
    private String airline;
    private String[] flyDays;
    private Airport origin;
    private Airport destination;

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public double getCost() {
        return cost;
    }

    public double getDuration() {
        return duration;
    }

    public Time getFlydeparture() {
        return flydeparture;
    }

    public String getAirline() {
        return airline;
    }

    public String[] getFlyDays() {
        return flyDays;
    }

    public int getID() {
        return ID;
    }

    public Flight(double cost, int ID, double duration, Time flydeparture, String airline, String[] flyDays) {

        this.cost = cost;
        this.duration = duration;
        this.flydeparture = flydeparture;
        this.airline = airline;
        this.flyDays = flyDays;
        this.ID = ID;
    }

    public Flight(int ID, String airline){
        this.ID = ID;
        this.airline = airline;
    }



    public boolean equals(Object obj){
        if( obj == null || !obj.getClass().equals(this.getClass())){
            return false;
        }

        Flight aux = (Flight) obj;
        return aux.getAirline().equals(airline) && aux.getID() == ID;
    }
}
