package com.example.magyar_madarak.data.model.CrossRefTables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.magyar_madarak.data.model.bird.BirdEntity;
import com.example.magyar_madarak.data.model.constants.Color;

@Entity(
        tableName = "color_cross_refs",
        primaryKeys = {"birdId", "colorId"},
        foreignKeys = {
                @ForeignKey(
                        entity = BirdEntity.class,
                        parentColumns = "birdId",
                        childColumns = "birdId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Color.class,
                        parentColumns = "colorId",
                        childColumns = "colorId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("birdId"),
                @Index("colorId")
        }
)
public class ColorCrossRef {
    @NonNull
    public String birdId;
    @NonNull
    public String colorId;

    public ColorCrossRef(@NonNull String birdId, @NonNull String colorId) {
        this.birdId = birdId;
        this.colorId = colorId;
    }
}
