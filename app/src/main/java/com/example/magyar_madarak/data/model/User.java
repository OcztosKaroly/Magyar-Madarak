package com.example.magyar_madarak.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String userId;

    private String email;
    private boolean emailVerified;

    public User(@NonNull String userId,
                String email,
                boolean emailVerified) {
        this.userId = userId;
        this.email = email;
        this.emailVerified = emailVerified;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
