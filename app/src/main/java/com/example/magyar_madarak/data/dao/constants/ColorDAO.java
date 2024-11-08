package com.example.magyar_madarak.data.dao.constants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.constants.Color;

import java.util.List;

@Dao
public interface ColorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Color color);

    @Query("SELECT * FROM colors")
    LiveData<List<Color>> getAll();

    @Update
    void update(Color color);

    @Delete
    void delete(Color color);

    @Query("DELETE FROM colors WHERE colorId = :colorId")
    void deleteById(String colorId);

    @Query("DELETE FROM colors")
    void deleteAll();
}
