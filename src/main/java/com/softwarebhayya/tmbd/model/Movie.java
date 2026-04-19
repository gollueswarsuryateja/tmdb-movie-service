package com.softwarebhayya.tmbd.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Schema(description = "Movie entity")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    @SequenceGenerator(name = "movie_seq", sequenceName = "movie_sequence", allocationSize = 1)
    @Schema(description = "Auto-generated unique ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Title of the movie", example = "Inception")
    private String name;

    @Schema(description = "Director of the movie", example = "Christopher Nolan")
    private String director;

    @ElementCollection
    @Schema(description = "List of actors in the movie", example = "[\"Leonardo DiCaprio\", \"Joseph Gordon-Levitt\"]")
    private List<String> actors = new ArrayList<>();
}
