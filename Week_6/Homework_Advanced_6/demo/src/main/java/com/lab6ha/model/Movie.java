package com.lab6ha.model;

public class Movie {

    private int id;
    private String title;
    private int duration;
    private double score;

    public Movie() { }

    public Movie(int id, String title, int duration, double score) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.score = score;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}