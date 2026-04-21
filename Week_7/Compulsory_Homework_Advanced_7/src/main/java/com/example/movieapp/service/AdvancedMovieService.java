package com.example.movieapp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.domain.ActorEntity;
import com.example.movieapp.domain.MovieEntity;
import com.example.movieapp.domain.MovieListEntity;
import com.example.movieapp.dto.MovieResponse;
import com.example.movieapp.dto.UnrelatedMovieListResponse;
import com.example.movieapp.exception.BadRequestException;
import com.example.movieapp.exception.NoValidMovieSelectionException;
import com.example.movieapp.repository.MovieListRepository;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.service.mapper.ApiMapper;

@Service
@Transactional
public class AdvancedMovieService {

    private final MovieRepository movieRepository;
    private final MovieListRepository movieListRepository;
    private final ApiMapper apiMapper;

    public AdvancedMovieService(MovieRepository movieRepository,
                                MovieListRepository movieListRepository,
                                ApiMapper apiMapper) {
        this.movieRepository = movieRepository;
        this.movieListRepository = movieListRepository;
        this.apiMapper = apiMapper;
    }

    public UnrelatedMovieListResponse findUnrelatedMoviesGreaterThan(int minimumSize) {
        if (minimumSize < 0) {
            throw new BadRequestException("minimumSize must be greater than or equal to 0");
        }

        List<MovieEntity> movies = movieRepository.findAll().stream()
                .sorted(Comparator.comparing(MovieEntity::getId))
                .collect(Collectors.toList());

        if (movies.isEmpty()) {
            throw new NoValidMovieSelectionException("No movies are available for solver processing");
        }

        Model model = new Model("unrelated-movie-selection");
        BoolVar[] selected = model.boolVarArray("movie", movies.size());

        for (int i = 0; i < movies.size(); i++) {
            for (int j = i + 1; j < movies.size(); j++) {
                if (shareActor(movies.get(i), movies.get(j))) {
                    model.arithm(selected[i], "+", selected[j], "<=", 1).post();
                }
            }
        }

        IntVar totalSelected = model.intVar("totalSelected", 0, movies.size());
        model.sum(selected, "=", totalSelected).post();

        Solution bestSolution = model.getSolver().findOptimalSolution(totalSelected, Model.MAXIMIZE);
        if (bestSolution == null) {
            throw new NoValidMovieSelectionException("Solver could not find a valid set of unrelated movies");
        }

        List<MovieEntity> chosenMovies = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (bestSolution.getIntVal(selected[i]) == 1) {
                chosenMovies.add(movies.get(i));
            }
        }

        if (chosenMovies.size() <= minimumSize) {
            throw new NoValidMovieSelectionException(
                    "No unrelated movie list has a size greater than " + minimumSize);
        }

        MovieListEntity savedList = new MovieListEntity();
        savedList.setName("Solver selection > " + minimumSize);
        savedList.getMovies().addAll(chosenMovies);
        savedList = movieListRepository.save(savedList);

        UnrelatedMovieListResponse response = new UnrelatedMovieListResponse();
        response.setMinimumExclusiveSize(minimumSize);
        response.setSelectedMovieCount(chosenMovies.size());
        response.setSavedListId(savedList.getId());
        response.setSavedListName(savedList.getName());
        response.setMovies(chosenMovies.stream()
                .map(apiMapper::toMovieResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private boolean shareActor(MovieEntity first, MovieEntity second) {
        Set<Integer> firstActorIds = new HashSet<>();
        for (ActorEntity actor : first.getActors()) {
            firstActorIds.add(actor.getId());
        }
        for (ActorEntity actor : second.getActors()) {
            if (firstActorIds.contains(actor.getId())) {
                return true;
            }
        }
        return false;
    }
}
