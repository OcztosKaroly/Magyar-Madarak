package com.example.magyar_madarak.data.dao.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.user.User;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM Users WHERE userId = :userId")
    LiveData<User> getById(String userId);

    @Update
    void update(User user);

    @Query("DELETE FROM users WHERE userId = :userId")
    void deleteById(String userId);

    @Delete
    void delete(User user);
}
