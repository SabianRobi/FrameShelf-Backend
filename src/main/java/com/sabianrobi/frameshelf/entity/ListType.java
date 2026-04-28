package com.sabianrobi.frameshelf.entity;

public enum ListType {
    MOVIE,
    PERSON;

    public static ListType from(final String source) {
        if (source == null) return null;

        try {
            return ListType.valueOf(source.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid list type: " + source);
        }
    }
}
