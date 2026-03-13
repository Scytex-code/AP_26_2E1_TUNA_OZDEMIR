public class Person implements Profile, Comparable<Person> {
    private String name;
    private String email;

    public Person(String name, String email) {
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
    public int compareTo(Person other) {
        // Natural order: alphabetical by name
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Person: " + name;
    }
}