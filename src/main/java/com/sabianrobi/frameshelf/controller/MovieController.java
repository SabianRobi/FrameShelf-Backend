package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.service.MovieService;
import com.sabianrobi.frameshelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    // Should be /user/{userId}/list/{listId}/{movieId}
//    @GetMapping("/movies/{id}")
//    public ResponseEntity<MovieResponse> getMovie(final @PathVariable("id") Integer id) {
//        final MovieResponse movieResponse = movieService.getMovie(id);
//
//        return ResponseEntity.ok(movieResponse);
//    }

    // Should be included with the previous endpoint (/user/{userId}/list/{listId}/{movieId});
//    @GetMapping("/movies/{id}/cast")
//    public ResponseEntity<List<CastMemberResponse>> getMovieCast(final @PathVariable("id") Integer id) {
//        return ResponseEntity.ok(movieService.getMovieCredits(id));
//    }
}
