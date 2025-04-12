package org.fawry.moviesapp.Service;

import org.fawry.moviesapp.Exceptions.NotFoundException;
import org.fawry.moviesapp.entity.Movies;
import org.fawry.moviesapp.entity.Ratings;
import org.fawry.moviesapp.repository.MoviesRepository;
import org.fawry.moviesapp.repository.RatingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RatingsService {


    private final RatingsRepository ratingsRepository;
    private final MoviesRepository moviesRepository;
    public RatingsService(RatingsRepository ratingsRepository, MoviesRepository moviesRepository) {
        this.ratingsRepository = ratingsRepository;
        this.moviesRepository = moviesRepository;
    }


    @Transactional

    public String addRating(Ratings ratings){
        if (ratings.getMovies() == null || ratings.getMovies().getImdbID() == null) {
            throw new IllegalArgumentException("Movie or Movie ID must not be null");
        }
        Optional<Movies> movie = moviesRepository.findById(ratings.getMovies().getImdbID());
        if(movie.isPresent()) {
            Ratings rating = new Ratings();
            rating.setMovies(movie.get());
            rating.setRating(ratings.getRating());
            ratingsRepository.save(rating);
            return "Rating add";
        } else {
            throw new NotFoundException("Movie not found");
        }
    }


    public double AverageRatingForMovie(String imdbID){
        Optional<Movies> movie = moviesRepository.findById(imdbID);
        if (movie.isEmpty()) {
            throw new NotFoundException("Movie not found");
        }
        List<Ratings> ratings = ratingsRepository.findByMovies(movie.get());
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToDouble(Ratings::getRating)
                .average()
                .orElse(0.0);
    }
}
