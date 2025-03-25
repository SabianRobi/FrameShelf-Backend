package com.sabianrobi.frameshelf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabianrobi.frameshelf.entity.Actor;
import com.sabianrobi.frameshelf.entity.request.CreateActorRequest;
import com.sabianrobi.frameshelf.entity.request.GetActorsRequest;
import com.sabianrobi.frameshelf.entity.response.ActorResponse;
import com.sabianrobi.frameshelf.entity.response.SearchActorResponse;
import com.sabianrobi.frameshelf.entity.utility.ActorFilterDto;
import com.sabianrobi.frameshelf.entity.utility.SortDto;
import com.sabianrobi.frameshelf.mapper.ActorMapper;
import com.sabianrobi.frameshelf.repository.ActorRepository;
import com.sabianrobi.frameshelf.specification.ActorSpecification;
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
public class ActorService {
    @Autowired
    private ActorRepository repository;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private ActorMapper actorMapper;

    public Page<ActorResponse> getActors(final GetActorsRequest request) {
        // Create filter DTO
        final ActorFilterDto filterDto = ActorFilterDto.builder()
                .name(request.getName())
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
        final Specification<Actor> specification = ActorSpecification.getSpecification(filterDto);
        final Page<Actor> actors = repository.findAll(specification, pageRequest);

        // Map to DTO and return
        return actors.map(actorMapper::mapActorToActorResponse);
    }

    public ActorResponse getActor(final int id) {
        final Actor actor = repository.findById(id).orElseThrow();

        return actorMapper.mapActorToActorResponse(actor);
    }

    public ActorResponse createActor(final CreateActorRequest createActorRequest) {
        final Actor actor;

        // Get actor details from the TMDB API
        try {
            actor = tmdbService.createActor(createActorRequest.getId());
            repository.save(actor);
        } catch (final TmdbException e) {
            throw new RuntimeException(e);
        }

        return actorMapper.mapActorToActorResponse(actor);
    }

    public void deleteActor(final int id) {
        repository.deleteById(id);
    }

    public Page<SearchActorResponse> search(final String query, final int page) {
        // Search in the TMDB API
        try {
            return tmdbService.searchActor(query, page);
        } catch (TmdbException e) {
            throw new RuntimeException(e);
        }
    }

    // Utility methods

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
}
