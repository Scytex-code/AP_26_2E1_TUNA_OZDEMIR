package com.example.movieapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieapp.dto.ActorRequest;
import com.example.movieapp.dto.ActorResponse;
import com.example.movieapp.service.ActorService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    @Operation(summary = "Get all actors")
    public ResponseEntity<List<ActorResponse>> getActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an actor by id")
    public ResponseEntity<ActorResponse> getActor(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActor(id));
    }

    @PostMapping
    @Operation(summary = "Create a new actor")
    public ResponseEntity<ActorResponse> createActor(@Valid @RequestBody ActorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an actor")
    public ResponseEntity<ActorResponse> updateActor(@PathVariable Integer id,
                                                     @Valid @RequestBody ActorRequest request) {
        return ResponseEntity.ok(actorService.updateActor(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an actor")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }
}
