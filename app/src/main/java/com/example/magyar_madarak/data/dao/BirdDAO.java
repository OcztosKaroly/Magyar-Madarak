package com.example.magyar_madarak.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.magyar_madarak.data.model.Bird;

import java.util.List;

@Dao
public interface BirdDAO {
    @Query("SELECT * FROM birds")
    LiveData<List<Bird>> getAllBirds();

    @Query("SELECT * FROM birds WHERE id = :birdId")
    LiveData<Bird> getBirdById(String birdId);

    @Query("SELECT * FROM birds WHERE name = :birdName")
    LiveData<Bird> getBirdByName(String birdName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bird bird);
}
