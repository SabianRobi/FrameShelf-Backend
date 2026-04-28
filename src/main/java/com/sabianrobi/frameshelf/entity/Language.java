package com.sabianrobi.frameshelf.entity;

public enum Language {
    ENGLISH,
    HUNGARIAN,
    OTHER;

    public static Language from(final String source) {
        if (source == null) return null;

        try {
            return Language.valueOf(source.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid language: " + source);
        }
    }
}
