/**
 * Represents a city in the transportation network.
 * This class extends {@link Location} and includes specific demographic information,
 * such as the city's population.
 */
public final class City extends Location {
    /** The population count of the city. */
    private int population;

    /**
     * Constructs a new City instance.
     * * @param name       The name of the city.
     * @param x          The x-coordinate of the city.
     * @param y          The y-coordinate of the city.
     * @param population The total population count of the city.
     */
    public City(String name, double x, double y, int population) {
        super(name, x, y);
        this.population = population;
    }

    /**
     * Retrieves the population of the city.
     * * @return The population count.
     */
    public int getPopulation() { 
        return population; 
    }
}