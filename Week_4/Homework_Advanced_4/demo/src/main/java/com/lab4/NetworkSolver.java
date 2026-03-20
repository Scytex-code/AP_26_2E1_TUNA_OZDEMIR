package com.lab4;

import java.util.Optional;

import org.jgrapht.Graph;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.tour.TwoApproxMetricTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class NetworkSolver {

    public void solve(City city) {
        Graph<Intersection, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        
        city.getIntersections().forEach(graph::addVertex);
        
        for (Street s : city.getStreets()) {
            // Optional instead of "if (edge != null)" pattern:
            Optional.ofNullable(graph.addEdge(s.getFrom(), s.getTo()))
                .ifPresent(edge -> graph.setEdgeWeight(edge, s.getLength()));
        }

        // MST ve TSP can stay the same...
        var mst = new KruskalMinimumSpanningTree<>(graph).getSpanningTree();
        System.out.println("\n--- Minimum Cabling Cost (MST) ---");
        System.out.println("Total Length: " + (int)mst.getWeight() + " m");

        var tspRoute = new TwoApproxMetricTSP<Intersection, DefaultWeightedEdge>().getTour(graph);
        System.out.println("\n--- Maintenance Route (TSP 2-Approx) ---");
        System.out.println("Total Route Length: " + (int)tspRoute.getWeight() + " m");
        System.out.println("Route Path: " + tspRoute.getVertexList());
    }
}