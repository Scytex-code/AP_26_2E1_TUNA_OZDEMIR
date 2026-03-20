package com.lab4;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

public class App {
    public static void main(String[] args) {
        Faker faker = new Faker();
        Random random = new Random();
        City city = new City();

        // 1. Create 10 intersections with random coordinates
        List<Intersection> nodes = IntStream.range(0, 10)
                .mapToObj(i -> new Intersection(faker.address().streetName() + " Crossing", 
                                                random.nextDouble() * 500, 
                                                random.nextDouble() * 500))
                .collect(Collectors.toList());

        nodes.forEach(city::addIntersection);

        // 2. Connect every intersection to each other to create a "Complete Graph"
        // This prevents the "Graph is not complete" error in the TSP algorithm.
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Intersection from = nodes.get(i);
                Intersection to = nodes.get(j);
                
                // Calculate Euclidean distance to satisfy the Triangle Inequality (Required for Advanced)
                int length = (int) Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + 
                                           Math.pow(from.getY() - to.getY(), 2));
                
                // Add the street to the city model
                city.addStreet(new Street(faker.address().streetName(), length, from, to));
            }
        }

        // 3. Sort all streets by length and print them (Compulsory part)
        System.out.println("--- All Streets (Sorted by Length) ---");
        city.getStreets().stream()
            .sorted(Comparator.comparingInt(Street::getLength))
            .forEach(System.out::println);

        // 4. Stream Query: Find streets longer than 200m with at least 3 connections (Homework part)
        System.out.println("\n--- Special Streets (Length > 200m & 3+ Connections) ---");
        city.getSpecialStreets(200).forEach(System.out::println);

        // 5. Run Graph Algorithms (MST and TSP - Advanced part)
        NetworkSolver solver = new NetworkSolver();
        solver.solve(city);
    }
}