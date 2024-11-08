package com.example.magyar_madarak.data.dao.constants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.constants.Diet;

import java.util.List;

@Dao
public interface DietDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Diet diet);

    @Query("SELECT * FROM diets")
    LiveData<List<Diet>> getAll();

    @Update
    void update(Diet diet);

    @Delete
    void delete(Diet diet);

    @Query("DELETE FROM diets WHERE dietId = :dietId")
    void deleteById(String dietId);

    @Query("DELETE FROM diets")
    void deleteAll();
}
