package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.movie.*;
import com.sabianrobi.frameshelf.entity.person.*;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.EditItemInListRequest;
import com.sabianrobi.frameshelf.entity.request.GetUserListsRequest;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.mapper.CreditMapper;
import com.sabianrobi.frameshelf.mapper.MovieCreditMapper;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import com.sabianrobi.frameshelf.mapper.TvCreditMapper;
import com.sabianrobi.frameshelf.repository.*;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.people.PersonDb;
import info.movito.themoviedbapi.tools.TmdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
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

    @Autowired
    private TMDBService tmdbService;

    @Autowired
    private TMDBMapper tmdbMapper;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ProductionCompanyRepository productionCompanyRepository;

    @Autowired
    private ProductionCountryRepository productionCountryRepository;

    @Autowired
    private SpokenLanguageRepository spokenLanguageRepository;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private MovieInListRepository movieInListRepository;

    @Autowired
    private PersonInListRepository personInListRepository;

    @Autowired
    private MovieCastMemberRepository movieCastMemberRepository;

    @Autowired
    private MovieCrewMemberRepository movieCrewMemberRepository;

    @Autowired
    private MovieCreditMapper movieCreditMapper;

    @Autowired
    private MovieCreditRepository movieCreditRepository;

    @Autowired
    private TvCastMemberRepository tvCastMemberRepository;

    @Autowired
    private TvCrewMemberRepository tvCrewMemberRepository;

    @Autowired
    private TvCreditMapper tvCreditMapper;

    @Autowired
    private TvCreditRepository tvCreditRepository;

    // ----- List operations -----

    public java.util.List<List> getUserLists(final UUID userId, final GetUserListsRequest request) {
        final java.util.List<List> allLists = new ArrayList<>();
        final String type = request != null ? request.getType() : null;
        final String nameFilter = request != null ? request.getName() : null;

        // Determine whether to filter by name
        final boolean hasNameFilter = nameFilter != null && !nameFilter.trim().isEmpty();

        // If no type is specified or type is "MOVIE", include movie lists
        if (type == null || type.equalsIgnoreCase("MOVIE")) {
            if (hasNameFilter) {
                allLists.addAll(movieListRepository.findByUserIdAndNameContainingIgnoreCase(userId, nameFilter.trim()));
            } else {
                allLists.addAll(movieListRepository.findByUserId(userId));
            }
        }

        // If no type is specified or type is "PERSON", include person lists
        if (type == null || type.equalsIgnoreCase("PERSON")) {
            if (hasNameFilter) {
                allLists.addAll(personListRepository.findByUserIdAndNameContainingIgnoreCase(userId, nameFilter.trim()));
            } else {
                allLists.addAll(personListRepository.findByUserId(userId));
            }
        }

        // If type is specified but not recognized, throw exception
        if (type != null && !type.equalsIgnoreCase("MOVIE") && !type.equalsIgnoreCase("PERSON")) {
            throw new IllegalArgumentException("Invalid list type: " + type + ". Valid types are: MOVIE, PERSON");
        }

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
        } else if ("PERSON".equalsIgnoreCase(type)) {
            final PersonList personList = PersonList.builder()
                    .name(name)
                    .user(user)
                    .build();
            return personListRepository.save(personList);
        } else {
            throw new IllegalArgumentException("Invalid list type. Must be 'MOVIE' or 'PERSON'");
        }
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

    // ----- List item operations -----

    @Transactional
    public List addItemToList(final UUID listId, final AddItemToListRequest request, final UUID userId) {
        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            final MovieInList movieInList = createMovieInList(request, movieList);

            movieList.getMovies().add(movieInList);
            return movieListRepository.save(movieList);
        }

        final Optional<PersonList> personListOpt = personListRepository.findById(listId);
        if (personListOpt.isPresent()) {
            final PersonList personList = personListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            final PersonInList personInList = createPersonInList(request, personList);

            personList.getPeople().add(personInList);
            return personListRepository.save(personList);
        }

        throw new RuntimeException("List not found");
    }

    @Transactional
    public List editItemInList(final UUID listId,
                               final UUID itemId,
                               final EditItemInListRequest request,
                               final UUID userId) {

        final MovieList movieList = getMovieList(listId, userId);
        final PersonList personList = getPersonList(listId, userId);

        if (movieList != null) {
            return editMovieInList(movieList, itemId, request, listId);
        } else if (personList != null) {
            return editPersonInList(personList, itemId, request, listId);
        } else {
            throw new RuntimeException("List not found");
        }
    }

    @Transactional
    public List removeItemFromList(final UUID listId, final UUID itemId, final UUID userId) {
        final MovieList movieList = getMovieList(listId, userId);
        final PersonList personList = getPersonList(listId, userId);

        if (movieList != null) {
            final MovieInList movieInList = getMovieInList(movieList, itemId);

            // Remove the item from the list
            movieList.getMovies().remove(movieInList);
            movieInListRepository.delete(movieInList);

            return movieListRepository.save(movieList);

        } else if (personList != null) {
            final PersonInList personInList = getPersonInList(personList, itemId);

            // Remove the item from the list
            personList.getPeople().remove(personInList);
            personInListRepository.delete(personInList);

            return personListRepository.save(personList);
        } else {
            throw new RuntimeException("List not found");
        }
    }

    // ----- Helper methods -----

    private MovieInList createMovieInList(final AddItemToListRequest request,
                                          final MovieList movieList
    ) {
        final Integer itemId = request.getItemId();

        // 1. Check if we already have the movie in our db. If so, use it.
        final Optional<Movie> existingMovieOpt = movieRepository.findById(itemId);
        if (existingMovieOpt.isPresent()) {
            final Movie existingMovie = existingMovieOpt.get();

            return MovieInList.builder()
                    .movie(existingMovie)
                    .list(movieList)
                    .addedAt(LocalDateTime.now())
                    .notes(request.getNotes())
                    .watchedAt(request.getWatchedAt())
                    .build();
        }

        // 2. Fetch movie details from external API (omitted here)
        MovieDb movieDb;
        try {
            movieDb = tmdbService.searchMovie(itemId);
        } catch (final TmdbException e) {
            throw new RuntimeException(e);
        }

        // 3. Create Movie object in db with all related entities
        final Collection belongsToCollection =
                tmdbMapper.mapTMDBCollectionToCollection(movieDb.getBelongsToCollection());
        if (belongsToCollection != null) {
            collectionRepository.findById(belongsToCollection.getId())
                    .orElseGet(() -> collectionRepository.save(belongsToCollection));
        }

        final Set<Genre> genres = movieDb.getGenres().stream()
                .map(tmdbGenre -> {
                    final Genre mapped = tmdbMapper.mapTMDBGenreToGenre(tmdbGenre);
                    final Optional<Genre> existing = genreRepository.findById(mapped.getId());

                    return existing.orElseGet(() -> genreRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Set<ProductionCompany> productionCompanies = movieDb.getProductionCompanies().stream()
                .map(tmdbCompany -> {
                    final ProductionCompany mapped = tmdbMapper.mapTMDBProductionCompanyToProductionCompany(tmdbCompany);
                    final Optional<ProductionCompany> existing = productionCompanyRepository.findById(mapped.getId());

                    return existing.orElseGet(() -> productionCompanyRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Set<ProductionCountry> productionCountries = movieDb.getProductionCountries().stream()
                .map(tmdbCountry -> {
                    final ProductionCountry mapped = tmdbMapper.mapTMDBProductionCountryToProductionCountry(tmdbCountry);
                    final Optional<ProductionCountry> existing = productionCountryRepository.findById(mapped.getIso31661());

                    return existing.orElseGet(() -> productionCountryRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Set<SpokenLanguage> spokenLanguages = movieDb.getSpokenLanguages().stream()
                .map(tmdbLanguage -> {
                    final SpokenLanguage mapped = tmdbMapper.mapTMDBSpokenLanguageToSpokenLanguage(tmdbLanguage);
                    final Optional<SpokenLanguage> existing = spokenLanguageRepository.findById(mapped.getIso6391());

                    return existing.orElseGet(() -> spokenLanguageRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Set<CastMember> castMembers = movieDb.getCredits().getCast().stream()
                .map(tmdbCast -> {
                    final CastMember mapped = tmdbMapper.mapTMDBCastMemberToCastMember(tmdbCast);
                    final Optional<CastMember> existing = castMemberRepository.findById(mapped.getCreditId());

                    return existing.orElseGet(() -> castMemberRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Set<CrewMember> crewMembers = movieDb.getCredits().getCrew().stream()
                .map(tmdbCrew -> {
                    final CrewMember mapped = tmdbMapper.mapTMDBCrewMemberToCrewMember(tmdbCrew);
                    final Optional<CrewMember> existing = crewMemberRepository.findById(mapped.getCreditId());

                    return existing.orElseGet(() -> crewMemberRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final Credits credits = creditMapper.mapToCredits(castMembers, crewMembers);
        creditRepository.save(credits);

        final Movie movie = tmdbMapper.mapTMDBMovieToMovie(
                movieDb,
                belongsToCollection,
                genres,
                productionCompanies,
                productionCountries,
                spokenLanguages,
                credits);
        movieRepository.save(movie);

        // 4. Create MovieInList entry with request params
        return MovieInList.builder()
                .movie(movie)
                .list(movieList)
                .addedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .watchedAt(request.getWatchedAt())
                .build();
    }

    private PersonInList createPersonInList(final AddItemToListRequest request,
                                            final PersonList personList
    ) {
        final Integer itemId = request.getItemId();

        // 1. Check if we already have the person in our db. If so, use it.
        final Optional<Person> existingPersonOpt = personRepository.findById(itemId);
        if (existingPersonOpt.isPresent()) {
            final Person existingPerson = existingPersonOpt.get();

            return PersonInList.builder()
                    .person(existingPerson)
                    .list(personList)
                    .addedAt(LocalDateTime.now())
                    .notes(request.getNotes())
                    .build();
        }

        // 2. Fetch person details from external API (omitted here)
        PersonDb personDb;
        try {
            personDb = tmdbService.searchPerson(itemId);
        } catch (final TmdbException e) {
            throw new RuntimeException(e);
        }


        // 3. Create Person object in db with all related entities
        // MovieCredits
        final Set<MovieCastMember> movieCastMembers = personDb.getMovieCredits().getCast().stream()
                .map(tmdbCastMember -> {
                    final MovieCastMember mapped = tmdbMapper.mapTMDBMovieCastMemberToMovieCastMember(tmdbCastMember);
                    final Optional<MovieCastMember> existing = movieCastMemberRepository.findById(mapped.getCreditId());

                    return existing.orElseGet(() -> movieCastMemberRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());
        final Set<MovieCrewMember> movieCrewMembers = personDb.getMovieCredits().getCrew().stream()
                .map(tmdbCrewMember -> {
                    final MovieCrewMember mapped = tmdbMapper.mapTMDBMovieCrewMemberToMovieCrewMember(tmdbCrewMember);
                    final Optional<MovieCrewMember> existing = movieCrewMemberRepository.findById(mapped.getCreditId());

                    return existing.orElseGet(() -> movieCrewMemberRepository.save(mapped));
                })
                .collect(java.util.stream.Collectors.toSet());

        final MovieCredits movieCredits = movieCreditMapper.mapToMovieCredits(movieCastMembers, movieCrewMembers);
        movieCreditRepository.save(movieCredits);

        // TvCredits
        final Set<TvCastMember> tvCastMembers = personDb.getTvCredits().getCast().stream()
                .map(tmdbTvCastMember -> {
                    final TvCastMember castMember = tmdbMapper.mapTMDBTvCastMemberToTvCastMember(tmdbTvCastMember);
                    final Optional<TvCastMember> existing = tvCastMemberRepository.findById(castMember.getCreditId());

                    return existing.orElseGet(() -> tvCastMemberRepository.save(castMember));
                })
                .collect(java.util.stream.Collectors.toSet());
        final Set<TvCrewMember> tvCrewMembers = personDb.getTvCredits().getCrew().stream()
                .map(tmdbTvCrewMember -> {
                    final TvCrewMember crewMember = tmdbMapper.mapTMDBTvCrewMemberToTvCrewMember(tmdbTvCrewMember);
                    final Optional<TvCrewMember> existing = tvCrewMemberRepository.findById(crewMember.getCreditId());

                    return existing.orElseGet(() -> tvCrewMemberRepository.save(crewMember));
                })
                .collect(java.util.stream.Collectors.toSet());

        final TvCredits tvCredits = tvCreditMapper.mapToTvCredits(tvCastMembers, tvCrewMembers);
        tvCreditRepository.save(tvCredits);

        // Person
        final Person person = tmdbMapper.mapTMDBPersonToPerson(personDb, movieCredits, tvCredits);
        personRepository.save(person);

        // 4. Create PersonInList entry with request params
        return PersonInList.builder()
                .person(person)
                .list(personList)
                .addedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();
    }

    private MovieList getMovieList(final UUID listId, final UUID userId) {
        // Verify list ownership
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        MovieList movieList = null;

        if (movieListOpt.isPresent()) {
            movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
        }
        return movieList;
    }

    private PersonList getPersonList(final UUID listId, final UUID userId) {
        // Verify list ownership
        PersonList personList = null;
        final Optional<PersonList> personListOpt = personListRepository.findById(listId);

        if (personListOpt.isPresent()) {
            personList = personListOpt.get();
            if (!personList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }
        }
        return personList;
    }

    private MovieList editMovieInList(final MovieList movieList,
                                      final UUID itemId,
                                      final EditItemInListRequest request,
                                      final UUID listId) {
        // Verify item exists in list
        if (movieList.getMovies().stream().noneMatch(
                movieInList -> movieInList.getId().equals(itemId)
        )) {
            throw new RuntimeException("Item not found in list");
        }

        // Fetch the MovieInList entity
        final MovieInList movieInList = movieInListRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found in list"));

        // Edit the fields
        if (request.getNotes() != null) {
            movieInList.setNotes(request.getNotes());
        }

        if (request.getWatchedAt() != null) {
            movieInList.setWatchedAt(request.getWatchedAt());
        }

        // Save and return
        movieInListRepository.save(movieInList);

        // Fetch the updated list to ensure it contains the newly updated item
        return movieListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found after update"));
    }

    private PersonList editPersonInList(final PersonList personList,
                                        final UUID itemId,
                                        final EditItemInListRequest request,
                                        final UUID listId) {
        // Verify item exists in list
        if (personList.getPeople().stream().noneMatch(
                personInList -> personInList.getId().equals(itemId)
        )) {
            throw new RuntimeException("Item not found in list");
        }

        // Fetch the MovieInList entity
        final PersonInList personInList = personInListRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found in list"));

        // Edit the fields
        if (request.getNotes() != null) {
            personInList.setNotes(request.getNotes());
        }

        // Save and return
        personInListRepository.save(personInList);

        // Fetch the updated list to ensure it contains the newly updated item
        return personListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("List not found after update"));
    }

    private MovieInList getMovieInList(final MovieList movieList, final UUID itemId) {
        // Verify item exists in list
        if (movieList.getMovies().stream().noneMatch(
                movieInList -> movieInList.getId().equals(itemId)
        )) {
            throw new RuntimeException("Item not found in list");
        }

        // Fetch the MovieInList entity
        return movieInListRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found in list"));
    }

    private PersonInList getPersonInList(final PersonList personList, final UUID itemId) {
        // Verify item exists in list
        if (personList.getPeople().stream().noneMatch(
                movieInList -> movieInList.getId().equals(itemId)
        )) {
            throw new RuntimeException("Item not found in list");
        }

        // Fetch the MovieInList entity
        return personInListRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found in list"));
    }
}
