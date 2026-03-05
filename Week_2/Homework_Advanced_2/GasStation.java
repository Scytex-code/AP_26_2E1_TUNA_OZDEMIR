/**
 * Represents a gas station in the transportation network.
 * This class extends {@link Location} and adds specific information about fuel pricing.
 */
public final class GasStation extends Location {
    /** The price of gas at this station. */
    private double gasPrice;

    /**
     * Constructs a new GasStation.
     * * @param name     The name of the gas station.
     * @param x        The x-coordinate of the station.
     * @param y        The y-coordinate of the station.
     * @param gasPrice The price of fuel at this station.
     */
    public GasStation(String name, double x, double y, double gasPrice) {
        super(name, x, y);
        this.gasPrice = gasPrice;
    }

    /**
     * @return The current gas price at this station.
     */
    public double getGasPrice() {
        return gasPrice;
    }
}