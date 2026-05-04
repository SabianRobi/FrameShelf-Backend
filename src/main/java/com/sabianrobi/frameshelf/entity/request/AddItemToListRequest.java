package com.sabianrobi.frameshelf.entity.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sabianrobi.frameshelf.entity.ListType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddMovieToListRequest.class, name = "MOVIE"),
        @JsonSubTypes.Type(value = AddPersonToListRequest.class, name = "PERSON")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AddItemToListRequest {
    @NotNull
    private ListType type;

    @NotNull
    private Integer itemId;
    
    private String notes;
}
