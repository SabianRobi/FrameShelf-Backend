package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Person;
import com.sabianrobi.frameshelf.entity.PersonInList;
import com.sabianrobi.frameshelf.entity.response.PersonInListResponse;
import com.sabianrobi.frameshelf.entity.response.PersonResponse;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonResponse mapPersonToPersonResponse(final Person person) {
        return PersonResponse.builder()
                .adult(person.isAdult())
                .alsoKnownAs(person.getAlsoKnownAs())
                .biography(person.getBiography())
                .birthday(person.getBirthday())
                .deathday(person.getDeathday())
                .gender(person.getGender())
                .homepage(person.getHomepage())
                .id(person.getId())
                .imdbId(person.getImdbId())
                .knownForDepartment(person.getKnownForDepartment())
                .name(person.getName())
                .placeOfBirth(person.getPlaceOfBirth())
                .popularity(person.getPopularity())
                .profilePath(person.getProfilePath())
                .build();
    }

    public PersonInListResponse mapPersonToPersonInListResponse(final PersonInList personInList) {
        return PersonInListResponse.builder()
                .id(personInList.getId())
                .person(mapPersonToPersonResponse(personInList.getPerson()))
                .addedAt(personInList.getAddedAt())
                .notes(personInList.getNotes())
                .build();
    }

    public PersonInListResponse mapPersonInListToPersonInListResponse(final PersonInList personInList) {
        return PersonInListResponse.builder()
                .id(personInList.getId())
                .person(mapPersonToPersonResponse(personInList.getPerson()))
                .addedAt(personInList.getAddedAt())
                .notes(personInList.getNotes())
                .build();
    }
}
