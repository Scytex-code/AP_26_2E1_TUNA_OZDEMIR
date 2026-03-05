/**
 * Represents an airport in the transportation network.
 * This class extends {@link Location} and provides information regarding
 * the number of terminals available at the airport.
 */
public final class Airport extends Location {
    /** The number of terminals present at the airport. */
    private int numberOfTerminals;

    /**
     * Constructs a new Airport instance.
     * @param name              The name of the airport.
     * @param x                 The x-coordinate of the airport.
     * @param y                 The y-coordinate of the airport.
     * @param numberOfTerminals The total number of terminals at the airport.
     */
    public Airport(String name, double x, double y, int numberOfTerminals) {
        super(name, x, y);
        this.numberOfTerminals = numberOfTerminals;
    }

    /**
     * Retrieves the number of terminals.
     * @return The terminal count.
     */
    public int getNumberOfTerminals() { 
        return numberOfTerminals; 
    }
}