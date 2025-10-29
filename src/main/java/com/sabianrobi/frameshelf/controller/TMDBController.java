package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMoviesResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPeopleResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPersonResponse;
import com.sabianrobi.frameshelf.service.TMDBService;
import com.sabianrobi.frameshelf.service.UserService;
import info.movito.themoviedbapi.tools.TmdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TMDBController {
    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private UserService userService;

    // ----- MOVIES -----

    @GetMapping("/movies/search")
    public ResponseEntity<Page<SearchMoviesResponse>> searchMovies(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        try {
            final Page<SearchMoviesResponse> result = tmdbService.searchMovies(query, page);
            return ResponseEntity.ok(result);
        } catch (final TmdbException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/movies/search/{movieId}")
    public ResponseEntity<SearchMovieResponse> searchMovie(final @PathVariable("movieId") Integer movieId) {
        try {
            final SearchMovieResponse result = tmdbService.searchMovie(movieId);
            return ResponseEntity.ok(result);
        } catch (final TmdbException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/people/search")
    public ResponseEntity<Page<SearchPeopleResponse>> searchPeople(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        try {
            final Page<SearchPeopleResponse> result = tmdbService.searchPeople(query, page);
            return ResponseEntity.ok(result);
        } catch (final TmdbException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/people/search/{personId}")
    public ResponseEntity<SearchPersonResponse> searchPerson(final @PathVariable("personId") Integer personId) {
        try {
            final SearchPersonResponse result = tmdbService.searchPerson(personId);
            return ResponseEntity.ok(result);
        } catch (final TmdbException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(503).build();
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
