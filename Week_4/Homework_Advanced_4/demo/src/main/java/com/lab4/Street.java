package com.lab4;

public class Street implements Comparable<Street> {
    private String name;
    private int length;
    private Intersection from;
    private Intersection to;

    public Street(String name, int length, Intersection from, Intersection to) {
        this.name = name;
        this.length = length;
        this.from = from;
        this.to = to;
    }

    public String getName() { return name; }
    public int getLength() { return length; }
    public Intersection getFrom() { return from; }
    public Intersection getTo() { return to; }

    @Override
    public int compareTo(Street other) {
        return Integer.compare(this.length, other.length);
    }

    @Override
    public String toString() {
        return String.format("%s (%d m) connects %s and %s", name, length, from, to);
    }
}