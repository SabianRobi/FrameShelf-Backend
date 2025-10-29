package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@DiscriminatorValue("ACTOR")
public class PersonList extends List {
    @ManyToMany
    private Set<Person> people = new HashSet<>();
}

