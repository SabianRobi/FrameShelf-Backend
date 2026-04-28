package com.sabianrobi.frameshelf.utility;

import com.sabianrobi.frameshelf.entity.Language;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLanguageConverter implements Converter<String, Language> {
    @Override
    public Language convert(final @NonNull String source) {
        return Language.from(source);
    }
}
