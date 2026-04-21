package com.example.movieapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.movieapp.dao.ActorDAO;
import com.example.movieapp.dao.MovieDAO;
import com.example.movieapp.dao.MovieListDAO;
import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieList;

public class PartitioningService {

    private final MovieDAO movieDAO = new MovieDAO();
    private final ActorDAO actorDAO = new ActorDAO();
    private final MovieListDAO movieListDAO = new MovieListDAO();

    public List<MovieList> partitionMoviesIntoLists() throws SQLException {
        List<Movie> movies = movieDAO.findAll();
        List<Actor> actors = actorDAO.findAll();

        // Build graph: movies connected if they share an actor
        Map<Movie, Set<Movie>> graph = buildGraph(movies);

        // Find connected components
        List<Set<Movie>> components = findConnectedComponents(graph);

        // Partition each component into lists with minimal number, balanced size
        List<MovieList> movieLists = new ArrayList<>();
        for (Set<Movie> component : components) {
            movieLists.addAll(partitionComponent(component));
        }

        // Save to database
        for (MovieList list : movieLists) {
            movieListDAO.create(list);
        }

        return movieLists;
    }

    private Map<Movie, Set<Movie>> buildGraph(List<Movie> movies) throws SQLException {
        Map<Movie, Set<Movie>> graph = new HashMap<>();
        for (Movie movie : movies) {
            graph.put(movie, new HashSet<>());
        }

        // For each pair of movies, check if they share actors
        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                Movie m1 = movies.get(i);
                Movie m2 = movies.get(j);
                if (shareActor(m1, m2)) {
                    graph.get(m1).add(m2);
                    graph.get(m2).add(m1);
                }
            }
        }

        return graph;
    }

    private boolean shareActor(Movie m1, Movie m2) {
        Set<String> actors1 = new HashSet<>();
        for (Actor a : m1.getActors()) {
            actors1.add(a.getName());
        }
        for (Actor a : m2.getActors()) {
            if (actors1.contains(a.getName())) {
                return true;
            }
        }
        return false;
    }

    private List<Set<Movie>> findConnectedComponents(Map<Movie, Set<Movie>> graph) {
        List<Set<Movie>> components = new ArrayList<>();
        Set<Movie> visited = new HashSet<>();

        for (Movie movie : graph.keySet()) {
            if (!visited.contains(movie)) {
                Set<Movie> component = new HashSet<>();
                dfs(movie, graph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private void dfs(Movie movie, Map<Movie, Set<Movie>> graph, Set<Movie> visited, Set<Movie> component) {
        visited.add(movie);
        component.add(movie);
        for (Movie neighbor : graph.get(movie)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, component);
            }
        }
    }

    private List<MovieList> partitionComponent(Set<Movie> component) {
        List<Movie> movieList = new ArrayList<>(component);
        int n = movieList.size();
        if (n == 0) return new ArrayList<>();

        // Number of lists: ceil(sqrt(n)) or something, but to minimize number with balanced size
        // For simplicity, use greedy: sort by degree or random
        // But to make it simple, divide into k lists where k is minimal such that sizes differ by at most 1

        int k = (int) Math.ceil(Math.sqrt(n)); // Approximation
        List<MovieList> lists = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            lists.add(new MovieList("Partition " + (i + 1)));
        }

        // Assign movies to lists in round-robin
        int index = 0;
        for (Movie movie : movieList) {
            lists.get(index % k).getMovies().add(movie);
            index++;
        }

        return lists;
    }
}