package com.sabianrobi.frameshelf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
import com.sabianrobi.frameshelf.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private ObjectMapper objectMapper;
//
//    public Page<ActorResponse> getActors(final GetActorsRequest request) {
//        // Create filter DTO
//        final ActorFilterDto filterDto = ActorFilterDto.builder()
//                .name(request.getName())
//                .build();
//
//        // Parse and create sort orders
//        final List<SortDto> sortDtos = jsonStringToSortDto(request.getSort());
//        final List<Sort.Order> orders = new ArrayList<>();
//
//        if (sortDtos != null) {
//            for (final SortDto sortDto : sortDtos) {
//                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
//                        ? Sort.Direction.DESC : Sort.Direction.ASC;
//                orders.add(new Sort.Order(direction, sortDto.getField()));
//            }
//        }
//
//        // Create a page request with sorting
//        final PageRequest pageRequest = PageRequest.of(
//                request.getPage(),
//                request.getSize(),
//                Sort.by(orders)
//        );
//
//        // Apply specification and pagination
//        final Specification<Actor> specification = ActorSpecification.getSpecification(filterDto);
//        final Page<Actor> actors = repository.findAll(specification, pageRequest);
//
//        // Map to DTO and return
//        return actors.map(actorMapper::mapActorToActorResponse);
//    }
//
//    public ActorResponse getActor(final int id) {
//        final Actor actor = repository.findById(id).orElseThrow();
//
//        return actorMapper.mapActorToActorResponse(actor);
//    }
//
//    public ActorResponse createActor(final CreateActorRequest createActorRequest) {
//        final Actor actor;
//
//        // Get actor details from the TMDB API
//        try {
//            actor = tmdbService.createActor(createActorRequest.getId());
//            repository.save(actor);
//        } catch (final TmdbException e) {
//            throw new RuntimeException(e);
//        }
//
//        return actorMapper.mapActorToActorResponse(actor);
//    }
//
//    public void deleteActor(final int id) {
//        repository.deleteById(id);
//    }
//
//    public Page<SearchActorResponse> search(final String query, final int page) {
//        // Search in the TMDB API
//        try {
//            return tmdbService.searchActor(query, page);
//        } catch (TmdbException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    // Utility methods
//
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
