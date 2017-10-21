package Model.Graph.GraphStructures;


import java.util.List;

public class Flight {

    private String airline;
    private String flightNumber;
    private List<Day> days;
    private Airport origin;
    private Airport target;
    private Integer departureTime;
    private Integer flightDuration;
    private Double price;

    public Flight(String airline, String flightNumber, List<Day> days, Airport origin, Airport target,
                  int departureTime, int flightDuration, double price){
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.days = days;
        this.origin = origin;
        this.target = target;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.price = price;
    }


    public String getAirline() {
        return airline;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getTarget() {
        return target;
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

}
