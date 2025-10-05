package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.GoogleUser;
import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.User;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.repository.MovieRepository;
import com.sabianrobi.frameshelf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMapper movieMapper;

    public User likeMovie(final User user, final int movieId) {
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));

        if (user.getLikedMovies() == null) {
            user.setLikedMovies(new HashSet<>());
        }

        user.getLikedMovies().add(movie);
        return userRepository.save(user);
    }

    public Optional<User> findById(final UUID id) {
        return userRepository.findById(id);
    }

    public Page<MovieResponse> getLikedMovies(final UUID userId, final Pageable pageable) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getLikedMovies() == null || user.getLikedMovies().isEmpty()) {
            return Page.empty(pageable);
        }

        List<MovieResponse> likedMovies = user.getLikedMovies().stream()
                .map(movieMapper::mapMovieToMovieResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), likedMovies.size());

        // If the start is greater than the size of the list, return an empty page
        if (start >= likedMovies.size()) {
            return Page.empty(pageable);
        }

        List<MovieResponse> pageContent = likedMovies.subList(start, end);
        return new PageImpl<>(pageContent, pageable, likedMovies.size());
    }

    public User findOrCreateUserForGoogleUser(final GoogleUser googleUser) {
        // Try to find an existing user linked to this GoogleUser
        Optional<User> existingUser = userRepository.findByGoogleUser(googleUser);

        if (existingUser.isPresent()) {
            // Update last login time
            User user = existingUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            // Create a new user
            // Generate a username based on email (before the @ symbol)
            String email = googleUser.getEmail();
            String baseUsername = email.substring(0, email.indexOf('@'));

            // Check if username exists, if so, append a number
            String username = baseUsername;
            int counter = 1;
            while (userRepository.findByUsername(username).isPresent()) {
                username = baseUsername + counter++;
            }

            User newUser = User.builder()
                    .username(username)
                    .displayName(googleUser.getFullName())
                    .profilePicture(googleUser.getPicture())
                    .googleUser(googleUser)
                    .createdAt(LocalDateTime.now())
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            return userRepository.save(newUser);
        }
    }
}
