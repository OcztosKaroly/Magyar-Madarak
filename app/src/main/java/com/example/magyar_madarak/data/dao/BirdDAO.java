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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bird bird);

    @Query("SELECT * FROM birds WHERE birdId = :birdId")
    LiveData<Bird> getBirdById(String birdId);

    @Query("SELECT * FROM birds WHERE name = :birdName")
    LiveData<Bird> getBirdByName(String birdName);

    @Query("SELECT * FROM birds WHERE name IN (:names)")
    LiveData<List<Bird>> getBirdsByNames(List<String> names);

    @Query("SELECT * FROM birds")
    LiveData<List<Bird>> getAllBirds();

    @Query("SELECT colors FROM birds")
    LiveData<List<String>> getAllColors();

    @Query("SELECT shapes FROM birds")
    LiveData<List<String>> getAllShapes();

    @Query("SELECT habitats FROM birds")
    LiveData<List<String>> getAllHabitats();
}
