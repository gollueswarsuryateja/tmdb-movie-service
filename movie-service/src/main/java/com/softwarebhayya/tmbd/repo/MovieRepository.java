package com.softwarebhayya.tmbd.repo;

import com.softwarebhayya.tmbd.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// it will interact with db
//it is a db connection or db layer
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
