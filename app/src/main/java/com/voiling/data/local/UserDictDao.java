package com.voiling.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDictDao {
    @Query("SELECT * FROM user_dict WHERE surface = :surface LIMIT 1")
    UserDictEntry findBySurface(String surface);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(UserDictEntry entry);
}
