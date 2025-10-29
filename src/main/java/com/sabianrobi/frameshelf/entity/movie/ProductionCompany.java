package com.sabianrobi.frameshelf.entity.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCompany {
    @Id
    int id;

    String logoPath;
    String name;
    String originCountry;
}
