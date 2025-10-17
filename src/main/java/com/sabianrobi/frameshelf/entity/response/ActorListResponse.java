package com.sabianrobi.frameshelf.entity.response;

import com.sabianrobi.frameshelf.entity.ActorList;
import com.sabianrobi.frameshelf.mapper.ActorMapper;
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
public class ActorListResponse extends ListResponse {
    private Set<ActorResponse> actors;

    /**
     * Create an ActorListResponse from an ActorList entity
     *
     * @param actorList   The ActorList entity
     * @param actorMapper The ActorMapper to use for conversion
     * @return The ActorListResponse
     */
    public static ActorListResponse fromActorList(ActorList actorList, ActorMapper actorMapper) {
        if (actorList == null) {
            return null;
        }

        return ActorListResponse.builder()
                .id(actorList.getId())
                .name(actorList.getName())
                .userId(actorList.getUser() != null ? actorList.getUser().getId() : null)
                .actors(actorList.getActors() != null && actorMapper != null ?
                        actorList.getActors().stream()
                                .map(actorMapper::mapActorToActorResponse)
                                .collect(Collectors.toSet()) : null)
                .build();
    }
}
