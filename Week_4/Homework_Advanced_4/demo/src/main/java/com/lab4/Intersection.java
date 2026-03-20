package com.lab4;

import java.util.Objects;

public class Intersection implements Comparable<Intersection> {
    private String name;
    private double x, y; // Used for Advanced: Triangle Inequality

    public Intersection(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public int compareTo(Intersection other) {
        return this.name.compareTo(other.name);
    }

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
        return name + " (" + (int)x + "," + (int)y + ")";
    }
}