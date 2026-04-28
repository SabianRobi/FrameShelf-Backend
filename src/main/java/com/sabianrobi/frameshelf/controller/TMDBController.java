package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMoviesResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPeopleResponse;
import com.sabianrobi.frameshelf.entity.response.SearchPersonResponse;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import com.sabianrobi.frameshelf.service.TMDBService;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.people.PersonDb;
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
    private TMDBMapper tmdbMapper;

    // ----- MOVIES -----

    @GetMapping("/movies/search")
    public ResponseEntity<Page<SearchMoviesResponse>> searchMovies(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        final Page<SearchMoviesResponse> result = tmdbService.searchMovies(query, page);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/movies/search/{movieId}")
    public ResponseEntity<SearchMovieResponse> searchMovie(final @PathVariable("movieId") Integer movieId) {
        final MovieDb movieDb = tmdbService.searchMovie(movieId);

        final SearchMovieResponse response = tmdbMapper.mapTMDBMovieToSearchMovieResponse(movieDb);

        return ResponseEntity.ok(response);
    }

    // ----- PEOPLE -----

    @GetMapping("/people/search")
    public ResponseEntity<Page<SearchPeopleResponse>> searchPeople(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        final Page<SearchPeopleResponse> result = tmdbService.searchPeople(query, page);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/people/search/{personId}")
    public ResponseEntity<SearchPersonResponse> searchPerson(final @PathVariable("personId") Integer personId) {
        final PersonDb personDb = tmdbService.searchPerson(personId);
        final SearchPersonResponse response = tmdbMapper.mapTMDBPersonToSearchPersonResponse(personDb);

        return ResponseEntity.ok(response);
    }
}
