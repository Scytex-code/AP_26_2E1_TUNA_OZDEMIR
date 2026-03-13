package com.example;

import java.util.Objects;

/**
 * Represents an intersection with a unique name.
 * Implements Comparable to allow natural ordering by name.
 */
public class Intersection implements Comparable<Intersection> {
    private String name;

    public Intersection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Comparison based on name (required for Comparable interface)
    @Override
    public int compareTo(Intersection other) {
        return this.name.compareTo(other.name);
    }

    // Required for HashSet to identify and prevent duplicate objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}