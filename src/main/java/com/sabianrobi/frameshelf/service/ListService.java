package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.repository.MovieListRepository;
import com.sabianrobi.frameshelf.repository.MovieRepository;
import com.sabianrobi.frameshelf.repository.PersonListRepository;
import com.sabianrobi.frameshelf.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListService {
    @Autowired
    private MovieListRepository movieListRepository;

    @Autowired
    private PersonListRepository personListRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonRepository personRepository;

    public java.util.List<List> getUserLists(final UUID userId) {
        final java.util.List<List> allLists = new ArrayList<>();
        allLists.addAll(movieListRepository.findByUserId(userId));
        allLists.addAll(personListRepository.findByUserId(userId));

        return allLists;
    }

    public List getListById(final UUID listId, final UUID userId) {
        // Try to find as MovieList first
        final Optional<MovieList> movieList = movieListRepository.findById(listId);
        if (movieList.isPresent() && movieList.get().getUser().getId().equals(userId)) {
            return movieList.get();
        }

        // Try to find as ActorList
        final Optional<PersonList> actorList = personListRepository.findById(listId);
        if (actorList.isPresent() && actorList.get().getUser().getId().equals(userId)) {
            return actorList.get();
        }

        throw new RuntimeException("List not found or user doesn't have access");
    }

    @Transactional
    public List createList(final User user, final String name, final String type) {
        if ("MOVIE".equalsIgnoreCase(type)) {
            final MovieList movieList = MovieList.builder()
                    .name(name)
                    .user(user)
                    .build();
            return movieListRepository.save(movieList);
        } else if ("ACTOR".equalsIgnoreCase(type)) {
            final PersonList personList = PersonList.builder()
                    .name(name)
                    .user(user)
                    .build();
            return personListRepository.save(personList);
        } else {
            throw new IllegalArgumentException("Invalid list type. Must be 'MOVIE' or 'ACTOR'");
        }
    }

    @Transactional
    public List addItemToList(final UUID listId, final Integer itemId, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            final Movie movie = movieRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Movie not found"));

            movieList.getMovies().add(movie);
            return movieListRepository.save(movieList);
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            final Person person = personRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Actor not found"));

            personList.getPeople().add(person);
            return personListRepository.save(personList);
        }

        throw new RuntimeException("List not found");
    }

    @Transactional
    public List removeItemFromList(final UUID listId, final Integer itemId, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            movieList.getMovies().removeIf(movie -> movie.getId() == itemId);
            return movieListRepository.save(movieList);
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            personList.getPeople().removeIf(actor -> actor.getId() == itemId);
            return personListRepository.save(personList);
        }

        throw new RuntimeException("List not found");
    }

    @Transactional
    public List updateList(final UUID listId, final UpdateListRequest request, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
            movieList.setName(request.getName());
            return movieListRepository.save(movieList);
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
            personList.setName(request.getName());
            return personListRepository.save(personList);
        }

        throw new RuntimeException("List not found");
    }

    @Transactional
    public void deleteList(final UUID listId, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
            movieListRepository.deleteById(listId);
            return;
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
            personListRepository.deleteById(listId);
            return;
        }

        throw new RuntimeException("List not found");
    }
}
