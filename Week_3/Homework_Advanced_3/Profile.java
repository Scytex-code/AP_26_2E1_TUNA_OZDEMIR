public interface Profile {
    String getName();
    String getContactDetails();
    int getId(); // Each profile must have a unique ID for graph algorithms
}