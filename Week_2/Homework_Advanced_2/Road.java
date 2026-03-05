import java.util.Objects;

/**
 * Represents a road connection between two {@link Location} objects.
 * This class includes validation logic to ensure the physical length 
 * is not shorter than the Euclidean distance between the locations.
 */
public class Road {
    private RoadType type;
    private double length;
    private int speedLimit;
    private Location source;
    private Location destination;

    /**
     * Constructs a new Road instance.
     * * @param type        The type of the road (e.g., HIGHWAY, COUNTRY).
     * @param length      The length of the road in kilometers.
     * @param speedLimit  The maximum speed limit allowed on this road.
     * @param source      The starting {@link Location}.
     * @param destination The ending {@link Location}.
     * @throws IllegalArgumentException if the provided length is less than the calculated Euclidean distance.
     */
    public Road(RoadType type, double length, int speedLimit, Location source, Location destination) {
        this.type = type;
        this.speedLimit = speedLimit;
        this.source = source;
        this.destination = destination;

        double euclideanDistance = Math.sqrt(Math.pow(destination.getX() - source.getX(), 2) + Math.pow(destination.getY() - source.getY(), 2));

        if (length < euclideanDistance) {
            throw new IllegalArgumentException("Road length, can't be shorter than the distance between the locations!");
        }
        this.length = length;
    }

    public RoadType getType() { return type; }
    public void setType(RoadType type) { this.type = type; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public int getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(int speedLimit) { this.speedLimit = speedLimit; }

    public Location getSource() { return source; }
    public void setSource(Location source) { this.source = source; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }

    /**
     * Returns a string representation of the road details.
     * @return A formatted string containing road type, source, destination, length, and speed limit.
     */
    @Override
    public String toString() {
        return type + " Road: " + source.getName() + " -> " + destination.getName() + 
               " | Length: " + length + "km | Speed Limit: " + speedLimit + "km/h";
    }

    /**
     * Compares this road with another object for equality based on road type, 
     * length, speed limit, source, and destination.
     * @param o The object to compare with.
     * @return true if both objects are identical in value, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return type == road.type && Double.compare(length, road.length) == 0 
                && speedLimit == road.speedLimit && source.equals(road.source) 
                && destination.equals(road.destination);
    }

    /**
     * Generates a hash code for the road instance based on its fields.
     * @return An integer hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, length, speedLimit, source, destination);
    }
}