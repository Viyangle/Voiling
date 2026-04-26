package com.voiling.model;

public class TokenPronunciation {
    private final String surface;
    private final String reading;
    private final String phonetic;

    public TokenPronunciation(String surface, String reading, String phonetic) {
        this.surface = surface;
        this.reading = reading;
        this.phonetic = phonetic;
    }

    public String getSurface() {
        return surface;
    }

    public String getReading() {
        return reading;
    }

    public String getPhonetic() {
        return phonetic;
    }
}
