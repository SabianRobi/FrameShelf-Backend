package com.sabianrobi.frameshelf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.request.CreateMovieRequest;
import com.sabianrobi.frameshelf.entity.request.GetMoviesRequest;
import com.sabianrobi.frameshelf.entity.response.MovieResponse;
import com.sabianrobi.frameshelf.entity.utility.MovieFilterDto;
import com.sabianrobi.frameshelf.entity.utility.SortDto;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.repository.MovieRepository;
import com.sabianrobi.frameshelf.specification.MovieSpecification;
import info.movito.themoviedbapi.tools.TmdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private MovieMapper movieMapper;

    public Page<MovieResponse> getMovies(final GetMoviesRequest request) {
        // Create filter DTO
        final MovieFilterDto filterDto = MovieFilterDto.builder()
                .title(request.getTitle())
                .build();

        // Parse and create sort orders
        final List<SortDto> sortDtos = jsonStringToSortDto(request.getSort());
        final List<Sort.Order> orders = new ArrayList<>();

        if (sortDtos != null) {
            for (final SortDto sortDto : sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, sortDto.getField()));
            }
        }

        // Create page request with sorting
        final PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(orders)
        );

        // Apply specification and pagination
        final Specification<Movie> specification = MovieSpecification.getSpecification(filterDto);
        final Page<Movie> movies = repository.findAll(specification, pageRequest);

        // Map to DTO and return
        return movies.map(movie -> movieMapper.mapMovieToMovieResponse(movie));
    }

    public MovieResponse getMovie(final Integer id) {
        final Movie movie = repository.findById(id).orElseThrow();

        return movieMapper.mapMovieToMovieResponse(movie);
    }

    private List<SortDto> jsonStringToSortDto(final String jsonString) {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(jsonString, new TypeReference<>() {
            });
        } catch (final Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }

    public MovieResponse createMovie(final CreateMovieRequest createMovieRequest) {
        final Movie movie;

        // Get movie details from the TMDB API
        try {
            movie = tmdbService.createMovie(createMovieRequest.getId());
            repository.save(movie);
        } catch (final TmdbException e) {
            throw new RuntimeException(e);
        }

        return movieMapper.mapMovieToMovieResponse(movie);
    }
    
    public void deleteMovie(final Integer id) {
        repository.deleteById(id);
    }
}
