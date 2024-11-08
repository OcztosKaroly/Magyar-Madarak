package com.example.magyar_madarak.data.model.CrossRefTables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Habitat;

@Entity(
        tableName = "habitat_cross_refs",
        primaryKeys = {"birdId", "habitatId"},
        foreignKeys = {
                @ForeignKey(
                        entity = BirdEntity.class,
                        parentColumns = "birdId",
                        childColumns = "birdId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Habitat.class,
                        parentColumns = "habitatId",
                        childColumns = "habitatId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("birdId"),
                @Index("habitatId")
        }
)
public class HabitatCrossRef {
    @NonNull
    public String birdId;
    @NonNull
    public String habitatId;

    public HabitatCrossRef(@NonNull String birdId, @NonNull String habitatId) {
        this.birdId = birdId;
        this.habitatId = habitatId;
    }
}