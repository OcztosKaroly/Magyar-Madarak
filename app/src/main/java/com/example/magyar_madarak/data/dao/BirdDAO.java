package com.example.magyar_madarak.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.magyar_madarak.data.model.Bird;

import java.util.List;

@Dao
public interface BirdDAO {
    @Query("SELECT * FROM birds")
    LiveData<List<Bird>> getAllBirds();

    @Query("SELECT * FROM birds WHERE name = :birdName")
    LiveData<Bird> getBirdByName(String birdName);
}
