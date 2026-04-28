package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "list_type")
@Table(name = "user_lists")
public abstract class List extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    public ListType getListType() {
        if (this instanceof MovieList) {
            return ListType.MOVIE;
        } else if (this instanceof PersonList) {
            return ListType.PERSON;
        }
        
        return null;
    }
}
