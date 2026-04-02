package com.catalog.model;

import java.io.Serializable;
import java.util.List;

public class Resource implements Serializable {

    private String id;
    private String title;
    private String location;
    private String author;
    private int year;

    private List<String> keywords;

    public Resource(String id,String title,String location,String author,int year,List<String> keywords){

        this.id=id;
        this.title=title;
        this.location=location;
        this.author=author;
        this.year=year;
        this.keywords=keywords;
    }

    public String getId(){return id;}
    public String getTitle(){return title;}
    public String getLocation(){return location;}
    public String getAuthor(){return author;}
    public int getYear(){return year;}
    public List<String> getKeywords(){return keywords;}

    @Override
    public String toString() {
        return id + " | " + title + " | " + author + " | " + year;
    }
}