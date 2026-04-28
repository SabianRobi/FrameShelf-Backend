package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.entity.request.params.GetUserListsParams;
import com.sabianrobi.frameshelf.error.exception.NotFoundException;
import com.sabianrobi.frameshelf.repository.ListRepository;
import com.sabianrobi.frameshelf.repository.MovieListRepository;
import com.sabianrobi.frameshelf.repository.PersonListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.sabianrobi.frameshelf.utility.Helper.verifyUserHasAccessToList;

@Service
public class ListService {
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private MovieListRepository movieListRepository;

    @Autowired
    private PersonListRepository personListRepository;

    public Page getUserLists(final UUID userId, final GetUserListsParams params, final Pageable pageable) {
        // Determine the type of list to fetch based on the request
        final String type = params.getType();
        final String nameFilter = params.getName().isBlank() ? null : params.getName();

        // Determine whether to filter by name
        final boolean hasNameFilter = nameFilter != null && !nameFilter.trim().isEmpty();

        if (type == null) {
            return listRepository.findByUserId(userId, pageable);
        } else if (type.equalsIgnoreCase("movie")) {
            return hasNameFilter
                    ? movieListRepository.findByUserIdAndNameContainingIgnoreCase(userId, nameFilter, pageable)
                    : movieListRepository.findByUserId(userId, pageable);
        } else if (type.equalsIgnoreCase("person")) {
            return hasNameFilter
                    ? personListRepository.findByUserIdAndNameContainingIgnoreCase(userId, nameFilter, pageable)
                    : personListRepository.findByUserId(userId, pageable);
        }

        throw new IllegalArgumentException("Invalid list type");
    }

    public List getListById(final UUID listId, final UUID userId) {
        final List list = listRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("List not found"));

        verifyUserHasAccessToList(userId, list);

        return list;
    }

    @Transactional
    public List createList(final User user, final String name, final ListType type) {
        if (type == ListType.MOVIE) {
            final MovieList movieList = MovieList.builder()
                    .name(name)
                    .user(user)
                    .build();
            return movieListRepository.save(movieList);
        } else if (type == ListType.PERSON) {
            final PersonList personList = PersonList.builder()
                    .name(name)
                    .user(user)
                    .build();
            return personListRepository.save(personList);
        }

        throw new IllegalArgumentException("Invalid list type");
    }

    @Transactional
    public List updateList(final UUID listId, final UpdateListRequest request, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();

            verifyUserHasAccessToList(userId, movieList);

            movieList.setName(request.getName());
            return movieListRepository.save(movieList);
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();

            verifyUserHasAccessToList(userId, personList);

            personList.setName(request.getName());
            return personListRepository.save(personList);
        }

        throw new NotFoundException("List not found");
    }

    @Transactional
    public void deleteList(final UUID listId, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();

            verifyUserHasAccessToList(userId, movieList);

            movieListRepository.deleteById(listId);
            return;
        }

        // Try ActorList
        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
        if (actorListOpt.isPresent()) {
            final PersonList personList = actorListOpt.get();

            verifyUserHasAccessToList(userId, personList);

            personListRepository.deleteById(listId);
            return;
        }

        throw new NotFoundException("List not found");
    }
}
