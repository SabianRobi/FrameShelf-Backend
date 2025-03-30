package com.sabianrobi.frameshelf.service;

import com.sabianrobi.frameshelf.entity.ProductionCompany;
import com.sabianrobi.frameshelf.repository.ProductionCompanyRepository;
import info.movito.themoviedbapi.model.core.IdElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductionCompanyService {
    @Autowired
    private ProductionCompanyRepository repository;

    public Set<ProductionCompany> createProductionCompanies(final List<info.movito.themoviedbapi.model.core.ProductionCompany> tmdbProductionCompanies) {
        if (tmdbProductionCompanies.isEmpty()) {
            return new HashSet<>();
        }

        // Save new genres
        final ArrayList<ProductionCompany> newProductionCompanies = new ArrayList<>();

        tmdbProductionCompanies.forEach(productionCompany -> {
            if (!repository.existsById(productionCompany.getId())) {
                final ProductionCompany newProductionCompany = ProductionCompany.builder()
                        .id(productionCompany.getId())
                        .logoPath(productionCompany.getLogoPath())
                        .name(productionCompany.getName())
                        .originCountry(productionCompany.getOriginCountry())
                        .build();

                newProductionCompanies.add(newProductionCompany);
            }
        });

        if (!newProductionCompanies.isEmpty()) {
            repository.saveAll(newProductionCompanies);
        }

        // Get all production companies and return them
        return repository.findAllByIdIn(tmdbProductionCompanies.stream().map(IdElement::getId).collect(Collectors.toSet()));
    }
}
