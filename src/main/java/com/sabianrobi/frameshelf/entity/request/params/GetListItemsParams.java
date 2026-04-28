package com.sabianrobi.frameshelf.entity.request.params;

import com.sabianrobi.frameshelf.entity.ListType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListItemsParams {
    private ListType type;
}
