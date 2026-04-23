package com.voiling.data;

import java.util.Optional;

public interface UserDictionaryRepository {
    Optional<String> findByOriginal(String originalText);

    void saveOrUpdate(String originalText, String cnPhonetic);
}
