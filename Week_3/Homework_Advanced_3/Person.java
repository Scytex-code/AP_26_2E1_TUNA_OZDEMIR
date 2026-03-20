import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public abstract class Person implements Profile, Comparable<Person> {
    private int id;
    private String name;
    private LocalDate birthDate;
    // Relationship map: Target Profile -> Description of the relationship
    private Map<Profile, String> relationships = new HashMap<>();

    public Person(int id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public void addRelationship(Profile p, String description) {
        relationships.put(p, description);
    }

    public Map<Profile, String> getRelationships() {
        return relationships;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    public LocalDate getBirthDate() { return birthDate; }

    @Override
    public int compareTo(Person other) {
        // Natural order based on names
        return this.name.compareTo(other.getName());
    }
}