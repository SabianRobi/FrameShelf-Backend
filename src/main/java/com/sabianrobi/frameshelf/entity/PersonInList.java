package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonInList extends BaseEntity {
    @ManyToOne
    private Person person;

    @ManyToOne
    private PersonList list;

    private LocalDateTime addedAt;
    private String notes;
}
