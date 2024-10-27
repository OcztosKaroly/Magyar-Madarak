package com.example.magyar_madarak.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.Observation;

import java.util.List;

@Dao
public interface ObservationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Observation observation);

    @Query("SELECT * FROM Observations WHERE observationId = :observationId")
    LiveData<Observation> getObservationById(String observationId);

    @Query("SELECT * FROM observations WHERE userId = :userId")
    LiveData<List<Observation>> getAllObservationByUserId(String userId);

    @Update
    void updateObservation(Observation observation);

    @Query("DELETE FROM observations WHERE observationId = :observationId")
    void deleteObservationById(String observationId);

    @Query("DELETE FROM observations WHERE userId = :userId")
    void deleteAllObservationsByUserId(String userId);
}
