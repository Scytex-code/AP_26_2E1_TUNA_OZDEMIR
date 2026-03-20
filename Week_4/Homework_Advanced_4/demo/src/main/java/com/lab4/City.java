package com.lab4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class City {
    private Set<Intersection> intersections = new HashSet<>();
    private List<Street> streets = new LinkedList<>();

    public void addIntersection(Intersection i) { intersections.add(i); }
    public void addStreet(Street s) { streets.add(s); }

    public Set<Intersection> getIntersections() { return intersections; }
    public List<Street> getStreets() { return streets; }

    /**
     * Homework Query: Find streets longer than X and connecting at least 3 other streets.
     */
    public List<Street> getSpecialStreets(int minLength) {
        return streets.stream()
                .filter(s -> s.getLength() > minLength)
                .filter(s -> getConnectedStreetsCount(s) >= 3)
                .collect(Collectors.toList());
    }

    private long getConnectedStreetsCount(Street s) {
        // Count how many other streets meet at the endpoints of street 's'
        return streets.stream()
                .filter(other -> other != s)
                .filter(other -> other.getFrom().equals(s.getFrom()) || other.getTo().equals(s.getFrom()) ||
                                 other.getFrom().equals(s.getTo()) || other.getTo().equals(s.getTo()))
                .count();
    }
}