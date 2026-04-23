package com.sabianrobi.frameshelf.mapper;

import com.sabianrobi.frameshelf.entity.List;
import com.sabianrobi.frameshelf.entity.response.ListResponse;
import org.springframework.stereotype.Component;

@Component
public class ListMapper {
    public ListResponse mapListToListResponse(final List list) {
        return ListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .userId(list.getUser() != null ? list.getUser().getId() : null)
                .createdAt(list.getCreatedAt())
                .updatedAt(list.getUpdatedAt())
                .build();
    }
}
