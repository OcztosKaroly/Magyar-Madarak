package com.example.magyar_madarak.data.dao.constants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.constants.Habitat;

import java.util.List;

@Dao
public interface HabitatDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Habitat habitat);

    @Query("SELECT * FROM habitats")
    LiveData<List<Habitat>> getAll();

    @Update
    void update(Habitat habitat);

    @Delete
    void delete(Habitat habitat);

    @Query("DELETE FROM habitats WHERE habitatId = :habitatId")
    void deleteById(String habitatId);

    @Query("DELETE FROM habitats")
    void deleteAll();
}
