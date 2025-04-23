package org.fawry.moviesapp.controller;
import org.fawry.moviesapp.Service.MoviesService;
import org.fawry.moviesapp.dto.MovieDto;
import org.fawry.moviesapp.dto.MovieExternalResponse;
import org.fawry.moviesapp.entity.Movies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/movies")


public class MoviesController {

    private final MoviesService moviesService;

    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Movies>> getAllMovies() {
       List<Movies> response = moviesService.getAllMovies();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Movies> getMoviesByIdInternal(@RequestParam String id) {
        Movies response = moviesService.getMovieDetailsByIdInternal(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<List<Movies>> getMoviesByTitleInternal(@RequestParam String title) {
        List<Movies> response = moviesService.getMovieByTitleInternal(title);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movies> addMovie(@RequestBody Movies movies) {
        Movies response = moviesService.addMovie(movies);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable String id) {
        return ResponseEntity.ok( moviesService.deleteMovie(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch-add")
    public ResponseEntity<List<Movies>> addMovies(@RequestBody List<Movies> movies) {
        List<Movies> addedMovies = moviesService.addMoviesBatch(movies);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovies);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/batch-remove")
    public ResponseEntity<String> removeMovies(@RequestBody List<String> movieIds) {
        moviesService.removeMoviesBatch(movieIds);
        return ResponseEntity.status(HttpStatus.OK).body("Movies successfully removed.");
    }

}
