package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.movie.*;
import com.sabianrobi.frameshelf.entity.person.*;
import com.sabianrobi.frameshelf.entity.request.*;
import com.sabianrobi.frameshelf.error.exception.NotFoundException;
import com.sabianrobi.frameshelf.mapper.CreditMapper;
import com.sabianrobi.frameshelf.mapper.MovieCreditMapper;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import com.sabianrobi.frameshelf.mapper.TvCreditMapper;
import com.sabianrobi.frameshelf.repository.*;
import info.movito.themoviedbapi.model.movies.MovieDb;
import info.movito.themoviedbapi.model.people.PersonDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.sabianrobi.frameshelf.utility.Helper.verifyUserHasAccessToList;

@Service
public class ListItemService {
    @Autowired
    private MovieListRepository movieListRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private PersonListRepository personListRepository;

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

    public Page<MovieInList> getMovieListItems(final UUID userId, final UUID listId, final Pageable pageable) {
        final MovieList movieList = movieListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("Movie list not found"));

        verifyUserHasAccessToList(userId, movieList);

        return movieInListRepository.findByListId(listId, pageable);
    }

    public Page<PersonInList> getPersonListItems(final UUID userId, final UUID listId, final Pageable pageable) {
        final PersonList personList = personListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("Person list not found"));

        verifyUserHasAccessToList(userId, personList);

        return personInListRepository.findByListId(listId, pageable);
    }

    public ItemInList getItemInList(final UUID userId, final UUID listId, final UUID itemId, final ListType type) {
        if (type == ListType.MOVIE) {
            final MovieList movieList = movieListRepository.findById(listId)
                    .orElseThrow(() -> new NotFoundException("Movie list not found"));

            verifyUserHasAccessToList(userId, movieList);

            return movieInListRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Item not found in list"));
        } else if (type == ListType.PERSON) {
            final PersonList personList = personListRepository.findById(listId)
                    .orElseThrow(() -> new NotFoundException("Person list not found"));

            verifyUserHasAccessToList(userId, personList);

            return personInListRepository.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Item not found in list"));
        }

        throw new NotFoundException("Type not found");
    }

    @Transactional
    public ItemInList addItemToList(final UUID listId, final AddItemToListRequest request, final UUID userId) {
        if (request instanceof AddMovieToListRequest movieRequest) {
            final MovieList movieList = movieListRepository.findById(listId)
                    .orElseThrow(() -> new NotFoundException("Movie list not found"));

            verifyUserHasAccessToList(userId, movieList);

            final MovieInList movieInList = createMovieInList(movieRequest, movieList);

            return movieInListRepository.save(movieInList);

        } else if (request instanceof AddPersonToListRequest personRequest) {
            final PersonList personList = personListRepository.findById(listId)
                    .orElseThrow(() -> new NotFoundException("Person list not found"));

            verifyUserHasAccessToList(userId, personList);

            final PersonInList personInList = createPersonInList(personRequest, personList);

            return personInListRepository.save(personInList);
        }


        throw new NotFoundException("List not found");
    }

    @Transactional
    public ItemInList editItemInList(final UUID listId,
                                     final UUID itemId,
                                     final EditItemInListRequest request,
                                     final UUID userId) {

        if (request instanceof EditMovieInListRequest editMovieInListRequest) {
            final MovieList movieList = getMovieList(listId, userId);

            return editMovieInList(movieList, itemId, editMovieInListRequest);
        } else if (request instanceof EditPersonInListRequest editPersonInListRequest) {
            final PersonList personList = getPersonList(listId, userId);

            return editPersonInList(personList, itemId, editPersonInListRequest);
        } else {
            throw new NotFoundException("List not found");
        }
    }

    @Transactional
    public void removeItemFromList(final UUID listId, final UUID itemId, final UUID userId, final ListType type) {
        if (type == ListType.MOVIE) {
            final MovieList movieList = getMovieList(listId, userId);
            final MovieInList movieInList = getMovieInList(movieList, itemId);

            // Delete the item directly, no need to manipulate the collection
            movieInListRepository.delete(movieInList);
        } else if (type == ListType.PERSON) {
            final PersonList personList = getPersonList(listId, userId);
            final PersonInList personInList = getPersonInList(personList, itemId);

            // Delete the item directly, no need to manipulate the collection
            personInListRepository.delete(personInList);
        } else {
            throw new NotFoundException("List not found");
        }
    }

    // ----- Helper methods -----

    private MovieInList createMovieInList(final AddMovieToListRequest request,
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
                    .watchedLanguage(request.getWatchedLanguage())
                    .build();
        }

        // 2. Fetch movie details from external API (omitted here)
        final MovieDb movieDb = tmdbService.searchMovie(itemId);

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
        movieRepository.saveAndFlush(movie);

        // 4. Create MovieInList entry with request params
        return MovieInList.builder()
                .movie(movie)
                .list(movieList)
                .addedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .watchedAt(request.getWatchedAt())
                .watchedLanguage(request.getWatchedLanguage())
                .build();
    }

    private PersonInList createPersonInList(final AddPersonToListRequest request,
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
        final PersonDb personDb = tmdbService.searchPerson(itemId);

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
        personRepository.saveAndFlush(person);

        // 4. Create PersonInList entry with request params
        return PersonInList.builder()
                .person(person)
                .list(personList)
                .addedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();
    }

    private MovieList getMovieList(final UUID listId, final UUID userId) {
        final MovieList movieList = movieListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("Movie list not found"));

        verifyUserHasAccessToList(userId, movieList);

        return movieList;
    }

    private PersonList getPersonList(final UUID listId, final UUID userId) {
        final PersonList personList = personListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("Person list not found"));

        verifyUserHasAccessToList(userId, personList);

        return personList;
    }

    private MovieInList editMovieInList(final MovieList movieList,
                                        final UUID itemId,
                                        final EditMovieInListRequest request) {
        // Fetch the MovieInList entity
        final MovieInList movieInList = movieInListRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found in list"));

        // Verify the item belongs to the correct list
        if (!movieInList.getList().getId().equals(movieList.getId())) {
            throw new NotFoundException("Item not found in list");
        }

        // Edit the fields
        if (request.getNotes() != null) {
            movieInList.setNotes(request.getNotes());
        }

        if (request.getWatchedAt() != null) {
            movieInList.setWatchedAt(request.getWatchedAt());
        }

        if (request.getWatchedLanguage() != null) {
            movieInList.setWatchedLanguage(request.getWatchedLanguage());
        }

        // Save and return
        return movieInListRepository.save(movieInList);
    }

    private PersonInList editPersonInList(final PersonList personList,
                                          final UUID itemId,
                                          final EditPersonInListRequest request) {
        // Fetch the PersonInList entity
        final PersonInList personInList = personInListRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found in list"));

        // Verify the item belongs to the correct list
        if (!personInList.getList().getId().equals(personList.getId())) {
            throw new NotFoundException("Item not found in list");
        }

        // Edit the fields
        if (request.getNotes() != null) {
            personInList.setNotes(request.getNotes());
        }

        // Save and return
        return personInListRepository.save(personInList);
    }

    private MovieInList getMovieInList(final MovieList movieList, final UUID itemId) {
        // Fetch the MovieInList entity
        final MovieInList movieInList = movieInListRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found in list"));

        // Verify the item belongs to the correct list
        if (!movieInList.getList().getId().equals(movieList.getId())) {
            throw new NotFoundException("Item not found in list");
        }

        return movieInList;
    }

    private PersonInList getPersonInList(final PersonList personList, final UUID itemId) {
        // Fetch the PersonInList entity
        final PersonInList personInList = personInListRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found in list"));

        // Verify the item belongs to the correct list
        if (!personInList.getList().getId().equals(personList.getId())) {
            throw new NotFoundException("Item not found in list");
        }

        return personInList;
    }
}
