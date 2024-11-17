package com.example.magyar_madarak.data.model.user;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String userId;

    private String email;

    public User(@NonNull String userId,
                String email) {
        this.userId = userId;
        this.email = email;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
