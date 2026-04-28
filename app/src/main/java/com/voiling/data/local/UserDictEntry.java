package com.voiling.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_dict")
public class UserDictEntry {
    @PrimaryKey
    @NonNull
    private String surface;
    @NonNull
    private String phonetic;
    private long updatedAt;

    public UserDictEntry(@NonNull String surface, @NonNull String phonetic, long updatedAt) {
        this.surface = surface;
        this.phonetic = phonetic;
        this.updatedAt = updatedAt;
    }

    @NonNull
    public String getSurface() {
        return surface;
    }

    public void setSurface(@NonNull String surface) {
        this.surface = surface;
    }

    @NonNull
    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(@NonNull String phonetic) {
        this.phonetic = phonetic;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
