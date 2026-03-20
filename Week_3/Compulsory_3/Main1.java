import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main1 {
    public static void main(String[] args) {
        // Create a list holding both persons and companies
        List<Profile1> network = new ArrayList<>();
        
        network.add(new Person1("Zane Smith", "zane@example.com"));
        network.add(new Company1("Tech Innovations Ltd", "San Francisco"));
        network.add(new Person1("Alice Johnson", "alice@example.com"));
        network.add(new Company1("Alpha Analytics", "London"));

        // Sort the list using a Comparator on the shared interface method
        network.sort(new Comparator<Profile1>() {
            @Override
            public int compare(Profile1 p1, Profile1 p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });
        
        // Note: You can also use this modern lambda equivalent:
        // network.sort(Comparator.comparing(Profile::getName));

        // Display the sorted list
        System.out.println("--- Sorted List ---");
        for (Profile1 profile : network) {
            System.out.println(profile.toString() + " (" + profile.getContactDetails() + ")");
        }
    }
}