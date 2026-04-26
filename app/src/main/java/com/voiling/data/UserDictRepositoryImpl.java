package com.voiling.data;

import com.voiling.data.local.UserDictDao;
import com.voiling.data.local.UserDictEntry;

public class UserDictRepositoryImpl implements UserDictRepository {
    private final UserDictDao userDictDao;

    public UserDictRepositoryImpl(UserDictDao userDictDao) {
        this.userDictDao = userDictDao;
    }

    @Override
    public String findBySurface(String surface) {
        UserDictEntry entry = userDictDao.findBySurface(surface);
        return entry == null ? null : entry.getPhonetic();
    }

    @Override
    public void saveOrUpdate(String surface, String phonetic) {
        if (surface == null || surface.isBlank() || phonetic == null || phonetic.isBlank()) {
            return;
        }
        userDictDao.upsert(new UserDictEntry(surface, phonetic, System.currentTimeMillis()));
    }
}
