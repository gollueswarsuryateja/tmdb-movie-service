package com.softwarebhayya.tmbd.api;

import com.softwarebhayya.tmbd.model.Movie;
import com.softwarebhayya.tmbd.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/movies")
@Tag(name = "Movie API", description = "CRUD operations for managing movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Operation(summary = "Get all movies", description = "Returns a list of all movies in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all movies"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.readAll();
        log.info("Returning all movies, count: {}", movies.size());
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Get movie by ID", description = "Returns a single movie by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovies(
            @Parameter(description = "ID of the movie to retrieve", example = "1") @PathVariable Long id) {
        Movie movie = movieService.read(id);
        log.info("Returning movie with id {}", id);
        return ResponseEntity.ok(movie);
    }

    @Operation(summary = "Create a new movie", description = "Creates a new movie and returns the saved entity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or unknown fields", content = @Content),
            @ApiResponse(responseCode = "415", description = "Unsupported media type", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Movie> createMovie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Movie object to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            @RequestBody Movie movie) {
        Movie createdMovie = movieService.create(movie);
        log.info("created movie with id {}", createdMovie.getId());
        return ResponseEntity.ok(createdMovie);
    }

    @Operation(summary = "Update a movie", description = "Updates an existing movie by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "ID of the movie to update", example = "1") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated movie object",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            @RequestBody Movie movie) {
        Movie updatedMovie = movieService.update(id, movie);
        log.info("updated movie with id {}", id);
        return ResponseEntity.ok(updatedMovie);
    }

    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "ID of the movie to delete", example = "1") @PathVariable Long id) {
        movieService.delete(id);
        log.info("deleted movie with id {}", id);
        return ResponseEntity.noContent().build();
    }
}
