import java.util.Objects;

/**
 * An abstract base class representing a geographic location in the network.
 * This class is sealed, allowing only {@link City}, {@link Airport}, and {@link GasStation} 
 * to extend it, ensuring a controlled class hierarchy.
 */
public abstract sealed class Location permits City, Airport, GasStation {
    private String name;
    private double x, y;

    /**
     * Constructs a new Location with the specified name and coordinates.
     * @param name The name of the location.
     * @param x    The x-coordinate in the 2D plane.
     * @param y    The y-coordinate in the 2D plane.
     */
    public Location(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * @return The name of this location.
     */
    public String getName() { return name; }

    /**
     * @param name The new name to set for this location.
     */
    public void setName(String name) { this.name = name; }

    /**
     * @return The x-coordinate of this location.
     */
    public double getX() { return x; }

    /**
     * @return The y-coordinate of this location.
     */
    public double getY() { return y; }

    /**
     * Returns a string representation of the location including its name and coordinates.
     * @return A formatted string.
     */
    @Override
    public String toString() {
        return name + " at [" + x + ", " + y + "]";
    }

    /**
     * Compares this location with another object for equality based on name and coordinates.
     * @param o The object to compare with.
     * @return true if both objects are identical in value, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(x, location.x) == 0 && Double.compare(y, location.y) == 0 && name.equals(location.name);
    }

    /**
     * Generates a hash code for the location instance.
     * @return An integer hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, x, y);
    }
}