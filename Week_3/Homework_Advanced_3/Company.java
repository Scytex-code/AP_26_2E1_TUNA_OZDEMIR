public class Company implements Profile, Comparable<Company> {
    private int id;
    private String name;
    private String industry; // Specific property

    public Company(int id, String name, String industry) {
        this.id = id;
        this.name = name;
        this.industry = industry;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getContactDetails() {
        return "Industry: " + industry;
    }

    @Override
    public int compareTo(Company other) {
        return this.name.compareTo(other.name);
    }
}