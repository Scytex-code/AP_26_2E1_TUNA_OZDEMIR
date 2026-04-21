package com.example.movieapp.service.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.movieapp.dto.AuthRequest;
import com.example.movieapp.dto.AuthResponse;
import com.example.movieapp.dto.MovieRequest;
import com.example.movieapp.dto.MovieResponse;
import com.example.movieapp.dto.MovieScorePatchRequest;

@Component
@ConditionalOnProperty(value = "movie.client.enabled", havingValue = "true")
public class MovieApiClient implements ApplicationRunner {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String username;
    private final String password;

    public MovieApiClient(RestTemplate restTemplate,
                          @Value("${movie.client.base-url}") String baseUrl,
                          @Value("${movie.client.username}") String username,
                          @Value("${movie.client.password}") String password) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        String token = authenticate();
        listMovies(token);
    }

    public List<MovieResponse> listMovies(String token) {
        HttpEntity<Void> request = new HttpEntity<>(authorizedHeaders(token));
        ResponseEntity<List<MovieResponse>> response = restTemplate.exchange(
                baseUrl + "/api/movies",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<MovieResponse>>() { }
        );
        return response.getBody() == null ? Collections.emptyList() : response.getBody();
    }

    public MovieResponse createMovie(String token, MovieRequest requestBody) {
        HttpEntity<MovieRequest> request = new HttpEntity<>(requestBody, authorizedHeaders(token));
        return restTemplate.postForObject(baseUrl + "/api/movies", request, MovieResponse.class);
    }

    public MovieResponse updateMovie(String token, Integer movieId, MovieRequest requestBody) {
        HttpEntity<MovieRequest> request = new HttpEntity<>(requestBody, authorizedHeaders(token));
        ResponseEntity<MovieResponse> response = restTemplate.exchange(
                baseUrl + "/api/movies/" + movieId,
                HttpMethod.PUT,
                request,
                MovieResponse.class
        );
        return response.getBody();
    }

    public MovieResponse patchMovieScore(String token, Integer movieId, Float score) {
        MovieScorePatchRequest patchRequest = new MovieScorePatchRequest();
        patchRequest.setScore(score);
        HttpEntity<MovieScorePatchRequest> request = new HttpEntity<>(patchRequest, authorizedHeaders(token));
        ResponseEntity<MovieResponse> response = restTemplate.exchange(
                baseUrl + "/api/movies/" + movieId + "/score",
                HttpMethod.PATCH,
                request,
                MovieResponse.class
        );
        return response.getBody();
    }

    public void deleteMovie(String token, Integer movieId) {
        HttpEntity<Void> request = new HttpEntity<>(authorizedHeaders(token));
        restTemplate.exchange(baseUrl + "/api/movies/" + movieId, HttpMethod.DELETE, request, Void.class);
    }

    private String authenticate() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        AuthResponse response = restTemplate.postForObject(baseUrl + "/api/auth/login", authRequest, AuthResponse.class);
        return response == null ? "" : response.getToken();
    }

    private HttpHeaders authorizedHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
