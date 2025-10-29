package com.sabianrobi.frameshelf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private ObjectMapper objectMapper;

//    public Page<MovieResponse> getMovies(final GetMoviesRequest request) {
//        // Create filter DTO
//        final MovieFilterDto filterDto = MovieFilterDto.builder()
//                .title(request.getTitle())
//                .build();
//
//        // Parse and create sort orders
//        final List<SortDto> sortDTOs = jsonStringToSortDto(request.getSort());
//        final List<Sort.Order> orders = new ArrayList<>();
//
//        if (sortDTOs != null) {
//            for (final SortDto sortDto : sortDTOs) {
//                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
//                        ? Sort.Direction.DESC : Sort.Direction.ASC;
//                orders.add(new Sort.Order(direction, sortDto.getField()));
//            }
//        }
//
//        // Create page request with sorting
//        final PageRequest pageRequest = PageRequest.of(
//                request.getPage(),
//                request.getSize(),
//                Sort.by(orders)
//        );
//
//        // Apply specification and pagination
//        final Specification<Movie> specification = MovieSpecification.getSpecification(filterDto);
//        final Page<Movie> movies = repository.findAll(specification, pageRequest);
//
//        // Map to DTO and return
//        return movies.map(movie -> movieMapper.mapMovieToMovieResponse(movie));
//    }
//
//    public MovieResponse getMovie(final int id) {
//        final Movie movie = repository.findById(id).orElseThrow();
//
//        return movieMapper.mapMovieToMovieResponse(movie);
//    }
//
//    public List<CastMemberResponse> getMovieCredits(final int movieId) {
//        // Get cast members from the TMDB API
//        try {
//            return tmdbService.getMovieCredits(movieId);
//        } catch (final TmdbException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public MovieResponse createMovie(final CreateMovieRequest createMovieRequest) {
//        final Movie movie;
//
//        // Set default values for optional fields
//        if (createMovieRequest.getWatchedAt() == null) {
//            createMovieRequest.setWatchedAt(new Date());
//        }
//
//        if (createMovieRequest.getWatchedLanguage() == null) {
//            createMovieRequest.setWatchedLanguage("hu");
//        }
//
//        // Get movie details from the TMDB API
//        try {
//            movie = tmdbService.createMovie(createMovieRequest.getId(), createMovieRequest.getWatchedLanguage(), createMovieRequest.getWatchedAt());
//            repository.save(movie);
//        } catch (final TmdbException e) {
//            throw new RuntimeException(e);
//        }
//
//        return movieMapper.mapMovieToMovieResponse(movie);
//    }
//
//    // Utility methods
//    private List<SortDto> jsonStringToSortDto(final String jsonString) {
//        try {
//            return objectMapper.readValue(jsonString, new TypeReference<>() {
//            });
//        } catch (final Exception e) {
//            System.out.println("Exception: " + e);
//            return null;
//        }
//    }
}
