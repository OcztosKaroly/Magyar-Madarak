package com.example.magyar_madarak.data.dao.observation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.observation.Observation;

import java.util.List;

@Dao
public interface ObservationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Observation observation);

    @Query("SELECT * FROM Observations WHERE observationId = :observationId")
    LiveData<Observation> getById(String observationId);

    @Query("SELECT * FROM Observations WHERE observationId = :observationId")
    Observation findById(String observationId);

    @Query("SELECT * FROM observations")
    LiveData<List<Observation>> getAll();

    @Query("SELECT * FROM observations")
    List<Observation> getAllForSync();

    @Update
    void update(Observation observation);

    @Query("DELETE FROM observations WHERE observationId = :observationId")
    void deleteById(String observationId);

    @Query("DELETE FROM observations")
    void deleteAll();
}
