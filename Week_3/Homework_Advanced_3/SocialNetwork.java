import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialNetwork {
    private List<Profile> profiles = new ArrayList<>();
    private int timer = 0;

    public void addProfile(Profile p) {
        profiles.add(p);
    }

    // Importance is defined as the number of relationships
    public int computeImportance(Profile p) {
        if (p instanceof Person) {
            return ((Person) p).getRelationships().size();
        }
        // For companies, count how many persons have a relationship with them
        int count = 0;
        for (Profile other : profiles) {
            if (other instanceof Person && ((Person) other).getRelationships().containsKey(p)) {
                count++;
            }
        }
        return count;
    }

    public void printNetwork() {
        // Sort by importance (descending)
        profiles.sort((p1, p2) -> Integer.compare(computeImportance(p2), computeImportance(p1)));
        
        System.out.println("--- Social Network Rankings ---");
        for (Profile p : profiles) {
            System.out.println(p.getName() + " [Importance: " + computeImportance(p) + "]");
        }
    }

    // Advanced: Find Cut Vertices (Articulation Points) using Tarjan's Algorithm logic
    public List<Profile> findCutVertices() {
        Map<Integer, Integer> discoveryTime = new HashMap<>();
        Map<Integer, Integer> lowLink = new HashMap<>();
        Set<Profile> cutVertices = new HashSet<>();
        Map<Integer, List<Profile>> adjacencyList = buildAdjacencyList();
        
        timer = 0;
        for (Profile p : profiles) {
            if (!discoveryTime.containsKey(p.getId())) {
                dfs(p, null, discoveryTime, lowLink, cutVertices, adjacencyList);
            }
        }
        return new ArrayList<>(cutVertices);
    }

    private void dfs(Profile u, Profile parent, Map<Integer, Integer> disc, Map<Integer, Integer> low, Set<Profile> cutVertices, Map<Integer, List<Profile>> adj) {
        timer++;
        int currentTime = timer;
        disc.put(u.getId(), currentTime); 
        low.put(u.getId(), currentTime);  
        int children = 0;

        for (Profile v : adj.getOrDefault(u.getId(), new ArrayList<>())) {
            if (parent != null && v.getId() == parent.getId()) continue;

            if (disc.containsKey(v.getId())) {
                // Back-edge found
                low.put(u.getId(), Math.min(low.get(u.getId()), disc.get(v.getId())));
            } else {
                children++;
                dfs(v, u, disc, low, cutVertices, adj);
                low.put(u.getId(), Math.min(low.get(u.getId()), low.get(v.getId())));
                
                // Check if u is an articulation point
                if (parent != null && low.get(v.getId()) >= disc.get(u.getId())) {
                    cutVertices.add(u);
                }
            }
        }
        // Root case
        if (parent == null && children > 1) cutVertices.add(u);
    }

    private Map<Integer, List<Profile>> buildAdjacencyList() {
        Map<Integer, List<Profile>> adj = new HashMap<>();
        for (Profile p : profiles) {
            if (p instanceof Person) {
                for (Profile neighbor : ((Person) p).getRelationships().keySet()) {
                    adj.computeIfAbsent(p.getId(), k -> new ArrayList<>()).add(neighbor);
                    adj.computeIfAbsent(neighbor.getId(), k -> new ArrayList<>()).add(p);
                }
            }
        }
        return adj;
    }
}