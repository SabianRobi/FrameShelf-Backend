package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.SpokenLanguage;
import com.sabianrobi.frameshelf.repository.SpokenLanguageRepository;
import info.movito.themoviedbapi.model.core.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpokenLanguageService {
    @Autowired
    private SpokenLanguageRepository repository;

    public Set<SpokenLanguage> createSpokenLanguages(final List<Language> tmdbSpokenLanguages) {
        if (tmdbSpokenLanguages.isEmpty()) {
            return new HashSet<>();
        }

        // Save new spoken languages
        final ArrayList<SpokenLanguage> newSpokenLanguages = new ArrayList<>();

        tmdbSpokenLanguages.forEach(spokenLanguage -> {
            if (!repository.existsById(spokenLanguage.getIso6391())) {
                final SpokenLanguage newSpokenLanguage = SpokenLanguage.builder()
                        .iso6391(spokenLanguage.getIso6391())
                        .name(spokenLanguage.getName())
                        .english_name(spokenLanguage.getEnglishName())
                        .build();

                newSpokenLanguages.add(newSpokenLanguage);
            }
        });

        if (!newSpokenLanguages.isEmpty()) {
            repository.saveAll(newSpokenLanguages);
        }

        // Get all spoken languages and return them
        return repository.findAllByIso6391In(tmdbSpokenLanguages.stream().map(Language::getIso6391).collect(Collectors.toSet()));
    }
}

