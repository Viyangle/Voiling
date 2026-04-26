package com.voiling.data;

public interface UserDictRepository {
    String findBySurface(String surface);

    void saveOrUpdate(String surface, String phonetic);
}
