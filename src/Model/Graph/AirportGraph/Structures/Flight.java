package Model.Graph.AirportGraph.Structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Flight {

    private String airline;
    private String flightNumber;
    private Day day;
    private Integer departureTime;
    private Integer flightDuration;
    private Double price;
    private int tagCurrentTime;

    public Flight(String airline, String flightNumber, Day day, int departureTime,
                  int flightDuration, double price){
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.day = day;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.price = price;
        this.tagCurrentTime = 0;
    }

    public Flight(String airline, String flightNumber){
        this.airline = airline;
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public Integer getFlightDuration() {
        return flightDuration;
    }

    public Double getPrice() {
        return price;
    }

    public void setTagCurrentTime(int currentTime) {
        this.tagCurrentTime = currentTime;
    }

    public boolean departureOnDate(List<Day> days){
        return days.contains(this.day);
    }

    public int timeToNext() {
        return Day.closestTimeWithOffset(tagCurrentTime, day, departureTime);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) return  false;
        if(obj == this) return  true;

        if(!getClass().equals(obj.getClass())) {
            return false;
        }
        Flight aux = (Flight) obj;
        return this.airline.equals(aux.getAirline()) && this.flightNumber.equals(aux.getFlightNumber());
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = hash + 31 *((airline == null) ? 0 : airline.hashCode());
        hash = hash + 17 * ((flightNumber == null) ? 0 : flightNumber.hashCode());
        return hash;
    }
}
