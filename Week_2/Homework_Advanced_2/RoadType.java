/**
 * Represents the various categories of roads within the transportation network.
 * This enumeration ensures type safety when defining road properties.
 */
public enum RoadType {
    /** Represents a high-speed, multi-lane divided highway. */
    HIGHWAY,
    
    /** Represents a major road designed for faster traffic than local roads. */
    EXPRESSWAY,
    
    /** Represents a road typically found in rural areas, often with lower speed limits. */
    COUNTRY,
    
    /** Represents a standard street or road serving local traffic within a city. */
    LOCAL
}