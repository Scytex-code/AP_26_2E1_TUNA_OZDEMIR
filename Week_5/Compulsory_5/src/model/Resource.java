package model;

import java.io.Serializable;

public class Resource implements Serializable {
    private String id;
    private String title;
    private String location; // Dosya yolu veya URL
    private int year;
    private String author;

    public Resource(String id, String title, String location, int year, String author) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.author = author;
    }

    // Getters ve Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    
    @Override
    public String toString() {
        return "Resource{" + "id='" + id + '\'' + ", title='" + title + '\'' + 
               ", year=" + year + ", author='" + author + '\'' + '}';
    }
}