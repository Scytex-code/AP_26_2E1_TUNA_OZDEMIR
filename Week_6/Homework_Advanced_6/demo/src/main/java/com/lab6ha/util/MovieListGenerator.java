package com.lab6ha.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lab6ha.dao.MovieDAO;
import com.lab6ha.dao.MovieListDAO;
import com.lab6ha.model.Movie;

public class MovieListGenerator {

    private MovieDAO movieDAO = new MovieDAO();
    private MovieListDAO movieListDAO = new MovieListDAO();

    // Basit graph algoritmasıyla unrelated movie lists oluşturuyoruz
    public List<List<Movie>> generateUnrelatedLists() throws SQLException {
        List<Movie> movies = movieDAO.findAll();
        Map<Integer, Set<Integer>> movieToActors = new HashMap<>();

        // Her filmin aktörlerini al
        for (Movie m : movies) {
            movieToActors.put(m.getId(), new HashSet<>(MovieActorDAOHelper.getActorIdsForMovie(m.getId())));
        }

        List<List<Movie>> lists = new ArrayList<>();

        for (Movie movie : movies) {
            boolean placed = false;
            // Her listeyi dene
            for (List<Movie> list : lists) {
                if (isUnrelated(movie, list, movieToActors)) {
                    list.add(movie);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                List<Movie> newList = new ArrayList<>();
                newList.add(movie);
                lists.add(newList);
            }
        }

        // Boyut farklarını kontrol et ve mümkün olduğunca eşitle
        lists.sort((a, b) -> b.size() - a.size());

        // DB'ye kaydet
        int counter = 1;
        for (List<Movie> list : lists) {
            String listName = "MovieList_" + counter++;
            movieListDAO.saveListWithMovies(listName, list);
        }

        return lists;
    }

    private boolean isUnrelated(Movie movie, List<Movie> list, Map<Integer, Set<Integer>> movieToActors) {
        Set<Integer> actors1 = movieToActors.get(movie.getId());
        for (Movie m : list) {
            Set<Integer> actors2 = movieToActors.get(m.getId());
            Set<Integer> intersection = new HashSet<>(actors1);
            intersection.retainAll(actors2);
            if (!intersection.isEmpty()) return false; // Ortak aktör var
        }
        return true;
    }
}