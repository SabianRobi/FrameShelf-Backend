package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Person;
import com.sabianrobi.frameshelf.entity.response.PersonResponse;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonResponse mapPersonToPersonResponse(final Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday())
                .profilePath(person.getProfilePath())
                .build();
    }
}
