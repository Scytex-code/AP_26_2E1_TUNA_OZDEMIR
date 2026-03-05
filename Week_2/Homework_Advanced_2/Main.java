import java.util.ArrayList;
import java.util.List;
import java.util.Random;

    /**
     * Generates a large random problem instance for performance testing.
     * @param numLocations The number of random locations to generate.
     * @param numRoads The number of random roads to generate.
     * @return A {@link ProblemInstance} populated with random data.
     */
    public static ProblemInstance generateRandomInstance(int numLocations, int numRoads) {
        ProblemInstance network = new ProblemInstance();
        Random rand = new Random();
        List<Location> locs = new ArrayList<>();

        System.out.println("Generating " + numLocations + " locations...");
        for (int i = 0; i < numLocations; i++) {
            // Generate cities with random coordinates and random population
            City city = new City("City-" + i, rand.nextDouble() * 1000, rand.nextDouble() * 1000, rand.nextInt(1000000));
            network.addLocation(city);
            locs.add(city);
        }

        System.out.println("Generating " + numRoads + " roads...");
        int roadsAdded = 0;
        while (roadsAdded < numRoads) {
            Location src = locs.get(rand.nextInt(numLocations));
            Location dest = locs.get(rand.nextInt(numLocations));

            if (!src.equals(dest)) {
                // Calculate Euclidean distance to pass the validation in the Road class constructor
                double euclidean = Math.sqrt(Math.pow(dest.getX() - src.getX(), 2) + Math.pow(dest.getY() - src.getY(), 2));
                
                // Actual distance should be slightly greater than the Euclidean distance
                double actualLength = euclidean + rand.nextDouble() * 50; 

                Road road = new Road(RoadType.HIGHWAY, actualLength, 90 + rand.nextInt(40), src, dest);
                network.addRoad(road);
                roadsAdded++;
            }
        }
        return network;
    }

    public static void main(String[] args) {
        // STEP 1: Generate 5,000 Locations and 20,000 Roads
        ProblemInstance network = generateRandomInstance(5000, 20000);
        Location start = network.getLocations().get(0);      // City-0
        Location end = network.getLocations().get(4999);     // City-4999

        System.out.println("\nStarting Dijkstra Algorithm...");

        // Start Memory (RAM) Measurement
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Run garbage collector for accurate measurement
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // Start Time Measurement
        long startTime = System.nanoTime();

        // STEP 2: Execute the Algorithm (Shortest Path)
        Solution bestRoute = network.getBestRoute(start, end, false);

        // End Time and Memory Measurement
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // STEP 3: Print Results
        if (bestRoute != null) {
            System.out.println("Route found! Total Distance: " + bestRoute.getTotalDistance());
        } else {
            System.out.println("No route exists between the selected locations.");
        }

        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Memory Used: " + (memoryAfter - memoryBefore) / (1024.0 * 1024.0) + " MB");
    }