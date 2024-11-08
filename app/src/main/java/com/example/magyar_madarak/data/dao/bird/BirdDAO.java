package com.example.magyar_madarak.data.dao.bird;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.magyar_madarak.data.model.CrossRefTables.ColorCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.DietCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.HabitatCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.ShapeCrossRef;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Diet;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;

import java.util.List;

@Dao
public interface BirdDAO {
    @Transaction
    default void insert(Bird bird) {
        insertBase(bird.getBird());

        for (Diet diet: bird.getDiets()) {
            insertDietCrossRef(new DietCrossRef(bird.getBirdId(), diet.getDietId()));
        }
        for (Color color: bird.getColors()) {
            insertColorCrossRef(new ColorCrossRef(bird.getBirdId(), color.getColorId()));
        }
        for (Shape shape: bird.getShapes()) {
            insertShapeCrossRef(new ShapeCrossRef(bird.getBirdId(), shape.getShapeId()));
        }
        for (Habitat habitat: bird.getHabitats()) {
            insertHabitatCrossRef(new HabitatCrossRef(bird.getBirdId(), habitat.getHabitatId()));
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBase(BirdEntity birdEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDietCrossRef(DietCrossRef dietCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColorCrossRef(ColorCrossRef colorCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShapeCrossRef(ShapeCrossRef shapeCrossRef);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHabitatCrossRef(HabitatCrossRef habitatCrossRef);



    @Transaction
    @Query("SELECT * FROM birds WHERE birdId = :birdId")
    LiveData<Bird> getById(String birdId);

    @Transaction
    @Query("SELECT * FROM birds WHERE birdName LIKE '%' || :birdName || '%'")
    LiveData<List<Bird>> getAllByName(String birdName);

    @Transaction
    @Query("SELECT * FROM birds WHERE birdName IN (:birdNames)")
    LiveData<List<Bird>> getAllByNames(List<String> birdNames);

    @Transaction
    @Query("SELECT * FROM birds")
    LiveData<List<Bird>> getAll();



    @Update
    void updateBase(BirdEntity birdEntity);

    @Transaction
    default void update(Bird bird) {
        delete(bird.getBird());
        insert(bird);
    }



    @Transaction
    @Delete
    void delete(BirdEntity bird);

    @Transaction
    @Query("DELETE FROM birds WHERE birdId = :birdId")
    void deleteById(String birdId);

    @Query("DELETE FROM diet_cross_refs WHERE birdId = :birdId")
    void deleteDietCrossRefsByBirdId(String birdId);

    @Query("DELETE FROM color_cross_refs WHERE birdId = :birdId")
    void deleteColorCrossRefsByBirdId(String birdId);

    @Query("DELETE FROM shape_cross_refs WHERE birdId = :birdId")
    void deleteShapeCrossRefsByBirdId(String birdId);

    @Query("DELETE FROM habitat_cross_refs WHERE birdId = :birdId")
    void deleteHabitatCrossRefsByBirdId(String birdId);
}
