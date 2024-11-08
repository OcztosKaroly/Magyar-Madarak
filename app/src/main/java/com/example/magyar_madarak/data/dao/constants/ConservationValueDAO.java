package com.example.magyar_madarak.data.dao.constants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.constants.ConservationValue;

import java.util.List;

@Dao
public interface ConservationValueDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConservationValue conservationValue);

    @Query("SELECT * FROM conservation_values")
    LiveData<List<ConservationValue>> getAll();

    @Update
    void update(ConservationValue conservationValue);

    @Delete
    void delete(ConservationValue conservationValue);

    @Query("DELETE FROM conservation_values WHERE conservationId = :conservationId")
    void deleteById(String conservationId);

    @Query("DELETE FROM conservation_values")
    void deleteAll();
}
