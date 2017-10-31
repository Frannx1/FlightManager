package Model;

public enum  FlightPriority {
    TIME(0), PRICE(1), TOTAL_TIME(2);

    private int value;

    FlightPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
