package com.example.magyar_madarak.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM Users WHERE userId = :userId")
    LiveData<User> getUserById(String userId);

    @Update
    void updateUser(User user);

    @Query("DELETE FROM users WHERE userId = :userId")
    void deleteUserById(String userId);

    @Delete
    void deleteUser(User user);
}
