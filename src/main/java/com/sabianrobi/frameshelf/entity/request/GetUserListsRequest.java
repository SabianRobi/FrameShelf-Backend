package com.sabianrobi.frameshelf.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserListsRequest {
    private String type; // Optional: "MOVIE" or "PERSON", null returns both
}
