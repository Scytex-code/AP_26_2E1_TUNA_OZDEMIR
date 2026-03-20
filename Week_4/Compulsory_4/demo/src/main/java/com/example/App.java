package com.example;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        // 1. Create 10 intersections using Java Streams
        List<Intersection> nodes = IntStream.range(0, 10)
                .mapToObj(i -> new Intersection("v" + i))
                .collect(Collectors.toList());

        // 2. Create a HashSet and verify the "No Duplicates" property
        Set<Intersection> intersectionSet = new HashSet<>(nodes);
        // Attempting to add a duplicate (v0 already exists in the list)
        intersectionSet.add(new Intersection("v0")); 
        
        System.out.println("--- Intersection Set Verification ---");
        System.out.println("Total unique intersections (Should be 10): " + intersectionSet.size());

        // 3. Create a list of streets using LinkedList implementation
        List<Street> streets = new LinkedList<>();
        streets.add(new Street("Street 1", 500, nodes.get(0), nodes.get(1)));
        streets.add(new Street("Street 2", 200, nodes.get(1), nodes.get(2)));
        streets.add(new Street("Street 3", 800, nodes.get(2), nodes.get(3)));
        streets.add(new Street("Street 4", 100, nodes.get(3), nodes.get(4)));

        // 4. Sort the list of streets by length using a Method Reference as a Comparator
        streets.sort(Comparator.comparingInt(Street::getLength));

        System.out.println("\n--- Streets Sorted by Length ---");
        streets.forEach(System.out::println);
    }
}