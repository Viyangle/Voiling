package com.voiling.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserDictRepository implements UserDictRepository {
    private final Map<String, String> overrides = new ConcurrentHashMap<>();

    @Override
    public String findBySurface(String surface) {
        return overrides.get(surface);
    }

    @Override
    public void saveOrUpdate(String surface, String phonetic) {
        if (surface == null || surface.isBlank() || phonetic == null || phonetic.isBlank()) {
            return;
        }
        overrides.put(surface, phonetic.trim());
    }
}
