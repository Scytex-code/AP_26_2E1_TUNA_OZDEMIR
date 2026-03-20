public class Person1 implements Profile1, Comparable<Person1> {
    private String name;
    private String email;

    public Person1(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContactDetails() {
        return "Email: " + email;
    }

    @Override
    public int compareTo(Person1 other) {
        // Natural order: alphabetical by name
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Person: " + name;
    }
}