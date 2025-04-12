package org.fawry.moviesapp.repository;

import org.fawry.moviesapp.entity.Movies;
import org.fawry.moviesapp.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings,Integer> {

    List<Ratings> findByMovies(Movies movie);
}
