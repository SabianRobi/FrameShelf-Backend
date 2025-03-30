package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.Actor;
import com.sabianrobi.frameshelf.entity.response.ActorResponse;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorResponse mapActorToActorResponse(final Actor actor) {
        return ActorResponse.builder()
                .id(actor.getId())
                .name(actor.getName())
                .birthday(actor.getBirthday())
                .profilePath(actor.getProfilePath())
                .build();
    }
}
