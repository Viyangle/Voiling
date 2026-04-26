package com.voiling.nlp;

public class TokenReading {
    private final String surface;
    private final String reading;

    public TokenReading(String surface, String reading) {
        this.surface = surface;
        this.reading = reading;
    }

    public String getSurface() {
        return surface;
    }

    public String getReading() {
        return reading;
    }
}
