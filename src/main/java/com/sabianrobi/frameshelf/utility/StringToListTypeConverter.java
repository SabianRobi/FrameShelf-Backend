package com.sabianrobi.frameshelf.utility;

import com.sabianrobi.frameshelf.entity.ListType;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToListTypeConverter implements Converter<String, ListType> {
    @Override
    public ListType convert(final @NonNull String source) {
        return ListType.from(source);
    }
}
