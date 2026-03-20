public class Company1 implements Profile1, Comparable<Company1> {
    private String name;
    private String headquarters;

    public Company1(String name, String headquarters) {
        this.name = name;
        this.headquarters = headquarters;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContactDetails() {
        return "HQ: " + headquarters;
    }

    @Override
    public int compareTo(Company1 other) {
        // Natural order: alphabetical by name
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return "Company: " + name;
    }
}