import java.util.List;

/**
 * Represents a solution to the reachability problem.
 * Stores the path taken between locations and the total length of the journey.
 */
public class Solution {
    private List<Location> path;
    private double totalDistance;

    /**
     * Constructs a solution instance.
     * @param path The ordered list of {@link Location}s representing the path.
     * @param totalDistance The total length of the path in kilometers.
     */
    public Solution(List<Location> path, double totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public List<Location> getPath() { 
        return path; 
    }

    public double getTotalDistance() { 
        return totalDistance; 
    }

    @Override
    public String toString() {
        return "Solution found: Path " + path + " | Total Distance: " + totalDistance + "km";
    }
}