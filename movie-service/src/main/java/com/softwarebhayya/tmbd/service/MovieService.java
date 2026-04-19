package com.softwarebhayya.tmbd.service;

import com.softwarebhayya.tmbd.exception.invalidDataException;
import com.softwarebhayya.tmbd.exception.notFoundException;
import com.softwarebhayya.tmbd.model.Movie;
import com.softwarebhayya.tmbd.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // CRUD Operations

    //create movie
    public Movie create(Movie movie) {
        if(movie == null) {
            throw new invalidDataException("Movie cannot be null");
        }
        return movieRepository.save(movie);
    }

    // Read fetch from db
    public Movie read(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new notFoundException("Movie with id " + id + " not found"));
    }

    // Read all movies
    public List<Movie> readAll() {
        return movieRepository.findAll();
    }

    //update movie
    public Movie update(Long id, Movie movie) {
        if(movie == null || id == null) {
            throw new invalidDataException("Movie cannot be null");
        }
        //check if exists
        if (movieRepository.existsById(id)) {
            Movie existingMovie = movieRepository.getReferenceById(id);
            existingMovie.setName(movie.getName());
            existingMovie.setDirector(movie.getDirector());
            existingMovie.setActors(movie.getActors());
            return movieRepository.save(existingMovie);
        } else {
            throw new notFoundException("Movie with id " + id + " not found");
        }
    }

    //delete movie
    public void delete(Long id) {
        if(id == null) {
            throw new invalidDataException("Movie id cannot be null");
        }
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new notFoundException("Movie with id " + id + " not found");
        }
    }

}
