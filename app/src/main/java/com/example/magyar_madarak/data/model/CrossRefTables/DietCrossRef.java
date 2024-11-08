package com.example.magyar_madarak.data.model.CrossRefTables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Diet;

@Entity(
        tableName = "diet_cross_refs",
        primaryKeys = {"birdId", "dietId"},
        foreignKeys = {
                @ForeignKey(
                        entity = BirdEntity.class,
                        parentColumns = "birdId",
                        childColumns = "birdId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Diet.class,
                        parentColumns = "dietId",
                        childColumns = "dietId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("birdId"),
                @Index("dietId")
        }
)
public class DietCrossRef {
    @NonNull
    public String birdId;

    @NonNull
    public String dietId;

    public DietCrossRef(@NonNull String birdId, @NonNull String dietId) {
        this.birdId = birdId;
        this.dietId = dietId;
    }
}
