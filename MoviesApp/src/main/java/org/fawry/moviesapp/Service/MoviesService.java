package org.fawry.moviesapp.Service;
import org.fawry.moviesapp.Exceptions.ConflictDataException;
import org.fawry.moviesapp.Exceptions.NotFoundException;
import org.fawry.moviesapp.dto.MovieDto;
import org.fawry.moviesapp.dto.MovieExternalResponse;
import org.fawry.moviesapp.entity.Movies;
import org.fawry.moviesapp.repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
@Service
public class MoviesService {
    private final MoviesRepository moviesRepository;
    public MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }
    public List<Movies> getMovieByTitleInternal(String t) {
        List<Movies> movieByTitle = moviesRepository.findAllByTitle(t);
        if(!movieByTitle.isEmpty())return movieByTitle;
        throw new NotFoundException("not found");
    }
    public Movies getMovieDetailsByIdInternal(String imdbId) {
        Optional<Movies> movieById = moviesRepository.findById(imdbId);
        if(movieById.isPresent()){
            return movieById.get();
        }
        throw new NotFoundException("movie nor found");
    }
    public Movies addMovie(Movies movie) {
        Optional<Movies> movieById = moviesRepository.findById(movie.getImdbID());
        Optional<Movies> movieByTitle = moviesRepository.findByTitle(movie.getTitle());
        movieById.ifPresent(existingMovie -> {
            throw new ConflictDataException("Movie already exists by ID!");
        });
        movieByTitle.ifPresent(existingMovie -> {
            throw new ConflictDataException("Movie already exists by Title!");
        });
        return moviesRepository.save(movie);
    }
    public String deleteMovie(String id) {
        Movies movie = moviesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found!"));
        moviesRepository.deleteById(id);
        return "Deleted";
    }
    public List<Movies> getAllMovies(){
        return moviesRepository.findAll();
    }
    public List<Movies> addMoviesBatch(List<Movies> movies) {
        return moviesRepository.saveAll(movies);
    }
    public void removeMoviesBatch(List<String> movieIds) {
        moviesRepository.deleteAllById(movieIds);
    }
}
