package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.request.CreateMovieRequest;
import com.sabianrobi.frameshelf.entity.request.GetMoviesRequest;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getMovies(final @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         final @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         final @RequestParam(name = "sort", defaultValue = "[{\"field\":\"title\",\"direction\":\"desc\"}]") String sort) {
        final Page<MovieResponse> movies = movieService.getMovies(
                GetMoviesRequest.builder()
                        .page(page)
                        .size(pageSize)
                        .sort(sort)
                        .build());

        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovie(final @PathVariable("id") Integer id) {
        final MovieResponse movieResponse = movieService.getMovie(id);

        return ResponseEntity.ok(movieResponse);
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(final @RequestBody CreateMovieRequest createMovieRequest) {
        final MovieResponse movieResponse = movieService.createMovie(createMovieRequest);

        return ResponseEntity.ok(movieResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(final @PathVariable("id") Integer id) {
        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SearchMovieResponse>> search(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        return ResponseEntity.ok(movieService.search(query, page));
    }
}
