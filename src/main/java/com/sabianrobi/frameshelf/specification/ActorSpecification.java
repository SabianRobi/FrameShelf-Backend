package com.sabianrobi.frameshelf.specification;

import com.sabianrobi.frameshelf.entity.Actor;
import com.sabianrobi.frameshelf.entity.utility.ActorFilterDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ActorSpecification {

    public static Specification<Actor> getSpecification(final ActorFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), filterDto.getName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
