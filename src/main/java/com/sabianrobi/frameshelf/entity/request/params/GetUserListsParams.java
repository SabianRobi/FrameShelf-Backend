package com.sabianrobi.frameshelf.entity.request.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserListsParams {
    private String type; // Optional: "MOVIE" or "PERSON"
    private String name = ""; // Optional: filters lists by name (case-insensitive contains
}
