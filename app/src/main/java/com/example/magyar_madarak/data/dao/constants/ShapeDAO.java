package com.example.magyar_madarak.data.dao.constants;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.constants.Shape;

import java.util.List;

@Dao
public interface ShapeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Shape shape);

    @Query("SELECT * FROM shapes")
    LiveData<List<Shape>> getAll();

    @Update
    void update(Shape shape);

    @Delete
    void delete(Shape shape);

    @Query("DELETE FROM shapes WHERE shapeId = :shapeId")
    void deleteById(String shapeId);

    @Query("DELETE FROM shapes")
    void deleteAll();
}
