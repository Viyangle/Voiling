package com.voiling.data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserDictionaryRepository implements UserDictionaryRepository {
    private final Map<String, String> dictionary = new ConcurrentHashMap<>();

    @Override
    public Optional<String> findByOriginal(String originalText) {
        return Optional.ofNullable(dictionary.get(originalText));
    }

    @Override
    public void saveOrUpdate(String originalText, String cnPhonetic) {
        if (originalText == null || originalText.isBlank() || cnPhonetic == null || cnPhonetic.isBlank()) {
            return;
        }
        dictionary.put(originalText, cnPhonetic);
    }
}
