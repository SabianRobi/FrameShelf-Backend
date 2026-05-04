package com.sabianrobi.frameshelf.entity.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PersonInListResponse extends ItemInListResponse {
    private PersonResponse person;
}
