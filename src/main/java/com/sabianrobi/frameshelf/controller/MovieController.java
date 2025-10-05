package com.sabianrobi.frameshelf.controller;

import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.response.CastMemberResponse;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.entity.response.SearchMovieResponse;
import com.sabianrobi.frameshelf.entity.response.UserResponse;
import com.sabianrobi.frameshelf.security.CustomOAuth2User;
import com.sabianrobi.frameshelf.service.MovieService;
import com.sabianrobi.frameshelf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}/movies")
    public ResponseEntity<Page<MovieResponse>> getUserLikedMovies(
            @PathVariable("userId") final String userId,
            @RequestParam(name = "page", defaultValue = "0") final Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") final Integer pageSize) {
        try {
            final UUID userUuid = UUID.fromString(userId);
            final Page<MovieResponse> likedMovies = userService.getLikedMovies(
                    userUuid,
                    PageRequest.of(page, pageSize));

            return ResponseEntity.ok(likedMovies);
        } catch (final IllegalArgumentException e) {
            // Invalid UUID format
            return ResponseEntity.badRequest().build();
        } catch (final RuntimeException e) {
            // User isn't found or other error
            return ResponseEntity.notFound().build();
        }
    }

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

    @PostMapping("/movies/{id}/like")
    public ResponseEntity<UserResponse> likeMovie(
            @PathVariable("id") final Integer movieId,
            @AuthenticationPrincipal final CustomOAuth2User customOAuth2User) {

        final User user = customOAuth2User.getUser();
        final User updatedUser = userService.likeMovie(user, movieId);

        return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
    }
}
