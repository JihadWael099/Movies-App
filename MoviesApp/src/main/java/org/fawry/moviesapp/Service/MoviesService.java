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

    @Value("${omdb.api.key}")
    private String apiKey;
    private final String omdbUrl = "http://www.omdbapi.com/";

    private final MoviesRepository moviesRepository;

    public MoviesService(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    public MovieDto getMovieByTitleExternal(String t) {
        String url = String.format("%s?t=%s&apikey=%s", omdbUrl, t, apiKey);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, MovieDto.class);
    }

    public List<Movies> getMovieByTitleInternal(String t) {
        List<Movies> movieByTitle = moviesRepository.findAllByTitle(t);
        if(!movieByTitle.isEmpty())return movieByTitle;
        throw new NotFoundException("not found");
    }


    public MovieDto getMovieDetailsByIdExternal(String imdbId) {
        String url = String.format("%s?i=%s&apikey=%s", omdbUrl, imdbId, apiKey);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, MovieDto.class);
    }

    public Movies getMovieDetailsByIdInternal(String imdbId) {
        Optional<Movies> movieById = moviesRepository.findById(imdbId);
        if(movieById.isPresent()){
            return movieById.get();
        }
        throw new NotFoundException("movie nor found");
    }



    public MovieExternalResponse searchMoviesByTitleAndPageExternal(String t, int page) {
        String url = String.format("%s?s=%s&page=%d&apikey=%s", omdbUrl, t, page, apiKey);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, MovieExternalResponse.class);
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
        Optional<Movies> movieById = moviesRepository.findById(id);
        movieById.ifPresent(movie -> moviesRepository.deleteById(id));
        movieById.orElseThrow(() -> new NotFoundException("Movie not found!"));
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
