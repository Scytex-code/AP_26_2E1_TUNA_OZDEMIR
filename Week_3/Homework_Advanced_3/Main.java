import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();

        // Initialize Profiles
        Programmer alice = new Programmer(1, "Alice", LocalDate.of(1995, 10, 5), "Java");
        Designer bob = new Designer(2, "Bob", LocalDate.of(1998, 3, 12), "Figma");
        Programmer charlie = new Programmer(3, "Charlie", LocalDate.of(1992, 7, 20), "C++");
        Company techCorp = new Company(4, "TechCorp", "Software");

        // Establish Relationships
        // Alice is the bridge (Cut Vertex)
        alice.addRelationship(bob, "Friends");
        alice.addRelationship(charlie, "Classmates");
        alice.addRelationship(techCorp, "Employee");

        network.addProfile(alice);
        network.addProfile(bob);
        network.addProfile(charlie);
        network.addProfile(techCorp);

        // Print network ordered by importance
        network.printNetwork();

        // Detect critical connectivity nodes
        System.out.println("\n--- Connectivity Analysis ---");
        List<Profile> criticalNodes = network.findCutVertices();
        if (criticalNodes.isEmpty()) {
            System.out.println("The network is robust. No single cut vertex found.");
        } else {
            for (Profile p : criticalNodes) {
                System.out.println("Critical Node (Cut Vertex): " + p.getName());
            }
        }
    }
}