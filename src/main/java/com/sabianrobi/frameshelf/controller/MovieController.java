package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.response.CastMemberResponse;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.service.MovieService;
import com.sabianrobi.frameshelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieResponse> getMovie(final @PathVariable("id") Integer id) {
        final MovieResponse movieResponse = movieService.getMovie(id);

        return ResponseEntity.ok(movieResponse);
    }

    @GetMapping("/movies/{id}/cast")
    public ResponseEntity<List<CastMemberResponse>> getMovieCast(final @PathVariable("id") Integer id) {
        return ResponseEntity.ok(movieService.getMovieCredits(id));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<Page<SearchMovieResponse>> search(
            final @RequestParam(name = "query") String query,
            final @RequestParam(name = "page", defaultValue = "1") Integer page) {

        return ResponseEntity.ok(movieService.search(query, page));
    }
}
