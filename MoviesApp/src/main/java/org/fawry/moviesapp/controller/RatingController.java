package org.fawry.moviesapp.controller;

import org.fawry.moviesapp.Service.RatingsService;
import org.fawry.moviesapp.entity.Ratings;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/ratings")
@CrossOrigin(origins = "http://localhost:4200")
public class RatingController {

    private final RatingsService ratingsService;

    public RatingController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addRating(@RequestBody Ratings ratings){
        String response=ratingsService.addRating(ratings);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<Double> avgRating(@RequestParam String id){
        Double response=ratingsService.AverageRatingForMovie(id);
        return ResponseEntity.ok(response);
    }

}
