package com.example.movieapp.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

public class ActorRequest {

    @NotBlank
    private String name;

    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
