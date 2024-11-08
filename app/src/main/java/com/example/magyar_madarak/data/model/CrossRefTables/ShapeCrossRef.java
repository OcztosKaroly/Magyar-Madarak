package com.example.magyar_madarak.data.model.CrossRefTables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Shape;

@Entity(
        tableName = "shape_cross_refs",
        primaryKeys = {"birdId", "shapeId"},
        foreignKeys = {
                @ForeignKey(
                        entity = BirdEntity.class,
                        parentColumns = "birdId",
                        childColumns = "birdId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Shape.class,
                        parentColumns = "shapeId",
                        childColumns = "shapeId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("birdId"),
                @Index("shapeId")
        }
)
public class ShapeCrossRef {
    @NonNull
    public String birdId;
    @NonNull
    public String shapeId;

    public ShapeCrossRef(@NonNull String birdId, @NonNull String shapeId) {
        this.birdId = birdId;
        this.shapeId = shapeId;
    }
}
