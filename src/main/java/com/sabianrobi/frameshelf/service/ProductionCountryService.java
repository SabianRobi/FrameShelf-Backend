package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.movie.ProductionCountry;
import com.sabianrobi.frameshelf.repository.ProductionCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionCountryService {
    @Autowired
    private ProductionCountryRepository repository;

    public Set<ProductionCountry> createProductionCountries(final List<info.movito.themoviedbapi.model.core.ProductionCountry> tmdbProductionCountries) {
        if (tmdbProductionCountries.isEmpty()) {
            return new HashSet<>();
        }

        // Save new genres
        final ArrayList<ProductionCountry> newProductionCountries = new ArrayList<>();

        tmdbProductionCountries.forEach(productionCountry -> {
            if (!repository.existsById(productionCountry.getIsoCode())) {
                final ProductionCountry newProductionCountry = ProductionCountry.builder()
                        .iso31661(productionCountry.getIsoCode())
                        .name(productionCountry.getName())
                        .build();

                newProductionCountries.add(newProductionCountry);
            }
        });

        if (!newProductionCountries.isEmpty()) {
            repository.saveAll(newProductionCountries);
        }

        // Get all production companies and return them
        return repository.findAllByIso31661In(tmdbProductionCountries.stream().map(info.movito.themoviedbapi.model.core.ProductionCountry::getIsoCode).collect(Collectors.toSet()));
    }
}
