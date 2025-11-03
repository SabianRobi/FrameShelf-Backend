package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.PersonList;
import com.sabianrobi.frameshelf.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonListResponse extends ListResponse {
    private Set<PersonInListResponse> people;

    /**
     * Create an ActorListResponse from an ActorList entity
     *
     * @param personList   The ActorList entity
     * @param personMapper The ActorMapper to use for conversion
     * @return The ActorListResponse
     */
    public static PersonListResponse fromPersonList(PersonList personList, PersonMapper personMapper) {
        if (personList == null) {
            return null;
        }

        return PersonListResponse.builder()
                .id(personList.getId())
                .name(personList.getName())
                .userId(personList.getUser() != null ? personList.getUser().getId() : null)
                .people(personList.getPeople() != null && personMapper != null ?
                        personList.getPeople().stream()
                                .map(personMapper::mapPersonToPersonInListResponse)
                                .collect(Collectors.toSet()) : null)
                .build();
    }
}
