package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.*;
import com.sabianrobi.frameshelf.entity.movie.*;
import com.sabianrobi.frameshelf.entity.request.AddItemToListRequest;
import com.sabianrobi.frameshelf.entity.request.UpdateListRequest;
import com.sabianrobi.frameshelf.mapper.CreditMapper;
import com.sabianrobi.frameshelf.mapper.MovieMapper;
import com.sabianrobi.frameshelf.mapper.TMDBMapper;
import com.sabianrobi.frameshelf.repository.*;
import info.movito.themoviedbapi.model.movies.MovieDb;
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
    private MovieMapper movieMapper;

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
    public List addItemToList(final UUID listId, final AddItemToListRequest request, final UUID userId) {
        final Integer itemId = request.getItemId();

        // Try MovieList first
        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
        if (movieListOpt.isPresent()) {
            final MovieList movieList = movieListOpt.get();
            if (!movieList.getUser().getId().equals(userId)) {
                throw new RuntimeException("User doesn't have access to this list");
            }

            // 1. Check if we already have the movie in our db. If so, use it.
            final Optional<Movie> existingMovieOpt = movieRepository.findById(itemId);
            if (existingMovieOpt.isPresent()) {
                final Movie existingMovie = existingMovieOpt.get();

                final MovieInList movieInList = MovieInList.builder()
                        .movie(existingMovie)
                        .list(movieList)
                        .addedAt(LocalDateTime.now())
                        .notes(request.getNotes())
                        .watchedAt(request.getWatchedAt())
                        .build();

                movieList.getMovies().add(movieInList);
                return movieListRepository.save(movieList);
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
            final MovieInList movieInList = MovieInList.builder()
                    .movie(movie)
                    .list(movieList)
                    .addedAt(LocalDateTime.now())
                    .notes(request.getNotes())
                    .watchedAt(request.getWatchedAt())
                    .build();

            // 5. Add to list and save
            movieList.getMovies().add(movieInList);
            return movieListRepository.save(movieList);
        }

        // Try ActorList
//        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
//        if (actorListOpt.isPresent()) {
//            final PersonList personList = actorListOpt.get();
//            if (!personList.getUser().getId().equals(userId)) {
//                throw new RuntimeException("User doesn't have access to this list");
//            }
//
//            final Person person = personRepository.findById(itemId)
//                    .orElseThrow(() -> new RuntimeException("Actor not found"));
//
//            personList.getPeople().add(person);
//            return personListRepository.save(personList);
//        }

        throw new RuntimeException("List not found");
    }

    //    @Transactional
//    public List removeItemFromList(final UUID listId, final Integer itemId, final UUID userId) {
//        // Try MovieList first
//        final Optional<MovieList> movieListOpt = movieListRepository.findById(listId);
//        if (movieListOpt.isPresent()) {
//            final MovieList movieList = movieListOpt.get();
//            if (!movieList.getUser().getId().equals(userId)) {
//                throw new RuntimeException("User doesn't have access to this list");
//            }
//
//            movieList.getMovies().removeIf(movie -> movie.getId() == itemId);
//            return movieListRepository.save(movieList);
//        }
//
//        // Try ActorList
//        final Optional<PersonList> actorListOpt = personListRepository.findById(listId);
//        if (actorListOpt.isPresent()) {
//            final PersonList personList = actorListOpt.get();
//            if (!personList.getUser().getId().equals(userId)) {
//                throw new RuntimeException("User doesn't have access to this list");
//            }
//
//            personList.getPeople().removeIf(actor -> actor.getId() == itemId);
//            return personListRepository.save(personList);
//        }
//
//        throw new RuntimeException("List not found");
//    }
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
