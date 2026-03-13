import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a list holding both persons and companies
        List<Profile> network = new ArrayList<>();
        
        network.add(new Person("Zane Smith", "zane@example.com"));
        network.add(new Company("Tech Innovations Ltd", "San Francisco"));
        network.add(new Person("Alice Johnson", "alice@example.com"));
        network.add(new Company("Alpha Analytics", "London"));

        // Sort the list using a Comparator on the shared interface method
        network.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile p1, Profile p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
        
        // Note: You can also use this modern lambda equivalent:
        // network.sort(Comparator.comparing(Profile::getName));

        // Display the sorted list
        System.out.println("--- Sorted List ---");
        for (Profile profile : network) {
            System.out.println(profile.toString() + " (" + profile.getContactDetails() + ")");
        }
    }
}