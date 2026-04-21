package com.example.movieapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.domain.ActorEntity;
import com.example.movieapp.dto.ActorRequest;
import com.example.movieapp.dto.ActorResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.service.mapper.ApiMapper;

@Service
@Transactional
public class ActorService {

    private final ActorRepository actorRepository;
    private final ApiMapper apiMapper;

    public ActorService(ActorRepository actorRepository, ApiMapper apiMapper) {
        this.actorRepository = actorRepository;
        this.apiMapper = apiMapper;
    }

    @Transactional(readOnly = true)
    public List<ActorResponse> getAllActors() {
        return actorRepository.findAll().stream()
                .map(apiMapper::toActorResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActorResponse getActor(Integer id) {
        return apiMapper.toActorResponse(findActorEntity(id));
    }

    public ActorResponse createActor(ActorRequest request) {
        ActorEntity actor = new ActorEntity();
        actor.setName(request.getName().trim());
        actor.setBirthDate(request.getBirthDate());
        return apiMapper.toActorResponse(actorRepository.save(actor));
    }

    public ActorResponse updateActor(Integer id, ActorRequest request) {
        ActorEntity actor = findActorEntity(id);
        actor.setName(request.getName().trim());
        actor.setBirthDate(request.getBirthDate());
        return apiMapper.toActorResponse(actorRepository.save(actor));
    }

    public void deleteActor(Integer id) {
        ActorEntity actor = findActorEntity(id);
        actorRepository.delete(actor);
    }

    public ActorEntity findActorEntity(Integer id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    }
}
