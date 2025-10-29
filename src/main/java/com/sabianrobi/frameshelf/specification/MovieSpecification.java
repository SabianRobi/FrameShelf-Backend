package com.sabianrobi.frameshelf.specification;

import com.sabianrobi.frameshelf.entity.Movie;
import com.sabianrobi.frameshelf.entity.utility.MovieFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {

    public static Specification<Movie> getSpecification(final MovieFilter filterDto) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getTitle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("title"), filterDto.getTitle()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
