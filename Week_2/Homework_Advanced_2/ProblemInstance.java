import java.util.*;

/**
 * Manages the entire transportation network, including collections of locations and roads.
 * Provides methods for adding components, validating network integrity, 
 * and performing reachability analysis between locations.
 */
public class ProblemInstance {
    private List<Location> locations;
    private List<Road> roads;

    /**
     * Initializes an empty ProblemInstance with new ArrayLists for locations and roads.
     */
    public ProblemInstance() {
        this.locations = new ArrayList<>();
        this.roads = new ArrayList<>();
    }

    public List<Location> getLocations() { return locations; }

    /**
     * Adds a location to the network if it does not already exist.
     * @param location The {@link Location} to add.
     */
    public void addLocation(Location location) {
        if (!locations.contains(location)) {
            locations.add(location);
        } else {
            System.out.println("Location has already been added: " + location.getName());
        }
    }

    /**
     * Adds a road to the network if it is valid and does not already exist.
     * The endpoints of the road must already exist in the network.
     * @param road The {@link Road} to add.
     */
    public void addRoad(Road road) {
        if (locations.contains(road.getSource()) && locations.contains(road.getDestination())) {
            if (!roads.contains(road)) {
                roads.add(road);
            } else {
                System.out.println("Road has already been added!");
            }
        } else {
            System.out.println("Error! Edges of the road should be in the network.");
        }
    }

    /**
     * Checks if the current problem instance is valid.
     * A valid instance must contain at least one location, and all roads must connect 
     * existing locations without self-loops.
     * @return true if the network is valid, false otherwise.
     */
    public boolean isValid() {
        if (locations.isEmpty()) return false;

        for (Road road : roads) {
            if (!locations.contains(road.getSource()) || !locations.contains(road.getDestination())) {
                return false;
            }
            if (road.getSource().equals(road.getDestination())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a path exists between two locations using Breadth-First Search (BFS).
     * @param start The starting {@link Location}.
     * @param end   The target {@link Location}.
     * @return true if there is a reachable path, false otherwise.
     */
    public boolean isReachable(Location start, Location end) {
        if (!locations.contains(start) || !locations.contains(end)) return false;

        Queue<Location> queue = new LinkedList<>();
        Set<Location> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(end)) return true;

            for (Road road : roads) {
                if (road.getSource().equals(current) && !visited.contains(road.getDestination())) {
                    visited.add(road.getDestination());
                    queue.add(road.getDestination());
                }
            }
        }
        return false;
    }

    /**
     * Calculates the total distance of a given path.
     * @param path A list of {@link Location}s representing the path.
     * @return A {@link Solution} object containing the path and the calculated distance.
     * @throws IllegalArgumentException if a segment between two locations is not found.
     */
    public Solution getSolution(List<Location> path) {
        double totalDistance = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            Location current = path.get(i);
            Location next = path.get(i + 1);
            boolean roadFound = false;

            for (Road road : roads) {
                if (road.getSource().equals(current) && road.getDestination().equals(next)) {
                    totalDistance += road.getLength();
                    roadFound = true;
                    break; 
                }
            }

            if (!roadFound) {
                throw new IllegalArgumentException("Road segment not found between " + current.getName() + " and " + next.getName());
            }
        }

        return new Solution(path, totalDistance);
    }

    /**
     * Finds the best route from start to end using Dijkstra's Algorithm.
     * @param start The starting location.
     * @param end The destination location.
     * @param isFastest If true, finds the fastest route (based on speed limit). If false, finds the shortest route.
     * @return A {@link Solution} object containing the optimal path, or null if no path exists.
     */
    public Solution getBestRoute(Location start, Location end, boolean isFastest) {
        if (!locations.contains(start) || !locations.contains(end)) return null;

        Map<Location, Double> costs = new HashMap<>();
        Map<Location, Location> previous = new HashMap<>();
        Set<Location> visited = new HashSet<>();

        // Temporary class to store current costs for the PriorityQueue
        class RouteNode implements Comparable<RouteNode> {
            Location location;
            double cost;
            RouteNode(Location loc, double c) { this.location = loc; this.cost = c; }
            public int compareTo(RouteNode other) { return Double.compare(this.cost, other.cost); }
        }

        PriorityQueue<RouteNode> pq = new PriorityQueue<>();

        // Initialize all distances to infinity initially
        for (Location loc : locations) {
            costs.put(loc, Double.MAX_VALUE);
        }
        
        costs.put(start, 0.0);
        pq.add(new RouteNode(start, 0.0));

        while (!pq.isEmpty()) {
            RouteNode current = pq.poll();
            Location currentLoc = current.location;

            if (visited.contains(currentLoc)) continue;
            visited.add(currentLoc);

            if (currentLoc.equals(end)) break; // Reached the destination, break the loop

            // Check all outgoing roads from this location
            for (Road road : roads) {
                if (road.getSource().equals(currentLoc)) {
                    Location neighbor = road.getDestination();
                    if (visited.contains(neighbor)) continue;

                    // Determine the weight (cost) based on whether the fastest or shortest route is requested
                    double weight = isFastest ? (road.getLength() / road.getSpeedLimit()) : road.getLength();
                    double newCost = costs.get(currentLoc) + weight;

                    // Update if the newly found path is better than the previous one
                    if (newCost < costs.get(neighbor)) {
                        costs.put(neighbor, newCost);
                        previous.put(neighbor, currentLoc); // Store where we came from to backtrack later
                        pq.add(new RouteNode(neighbor, newCost));
                    }
                }
            }
        }

        // Return null if the destination is unreachable
        if (!previous.containsKey(end) && !start.equals(end)) {
            return null;
        }

        // Construct the route by reading the "previous nodes" list in reverse
        List<Location> path = new ArrayList<>();
        Location step = end;
        while (step != null) {
            path.add(0, step); // Add to the beginning of the list to correct the order
            step = previous.get(step);
        }

        // Pass this route to the previously written getSolution method
        // It calculates the total distance and returns a clean Solution package!
        return getSolution(path);
    }

    /**
     * Returns a summary of the current network instance.
     * @return A string containing the count of locations and roads.
     */
    @Override
    public String toString() {
        return "Problem Instance: Contains " + locations.size() + " location(s) and " + roads.size() + " road(s).";
    }
}