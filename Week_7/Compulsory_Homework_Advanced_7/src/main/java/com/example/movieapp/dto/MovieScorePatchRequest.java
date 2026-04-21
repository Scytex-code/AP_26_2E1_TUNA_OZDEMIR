package com.example.movieapp.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class MovieScorePatchRequest {

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Float score;

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
