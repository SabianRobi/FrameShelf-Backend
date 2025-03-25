package com.sabianrobi.frameshelf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductionCompany {
    @Id
    int id;

    String logo_path;
    String name;
    String origin_country;
}
