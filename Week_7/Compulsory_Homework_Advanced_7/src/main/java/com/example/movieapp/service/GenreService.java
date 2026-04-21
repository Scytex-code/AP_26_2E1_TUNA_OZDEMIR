package com.example.movieapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.domain.GenreEntity;
import com.example.movieapp.dto.GenreRequest;
import com.example.movieapp.dto.GenreResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.repository.GenreRepository;
import com.example.movieapp.service.mapper.ApiMapper;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final ApiMapper apiMapper;

    public GenreService(GenreRepository genreRepository, ApiMapper apiMapper) {
        this.genreRepository = genreRepository;
        this.apiMapper = apiMapper;
    }

    @Transactional(readOnly = true)
    public List<GenreResponse> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(apiMapper::toGenreResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GenreResponse getGenre(Integer id) {
        return apiMapper.toGenreResponse(findGenreEntity(id));
    }

    public GenreResponse createGenre(GenreRequest request) {
        GenreEntity genre = new GenreEntity();
        genre.setName(request.getName().trim());
        return apiMapper.toGenreResponse(genreRepository.save(genre));
    }

    public GenreResponse updateGenre(Integer id, GenreRequest request) {
        GenreEntity genre = findGenreEntity(id);
        genre.setName(request.getName().trim());
        return apiMapper.toGenreResponse(genreRepository.save(genre));
    }

    public void deleteGenre(Integer id) {
        GenreEntity genre = findGenreEntity(id);
        genreRepository.delete(genre);
    }

    public GenreEntity findGenreEntity(Integer id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
    }
}
