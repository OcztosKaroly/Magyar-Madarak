package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shapes")
public class Shape {
    @PrimaryKey
    @NonNull
    private String shapeId;
    @NonNull
    private String shapeName;

    public Shape(@NonNull String shapeId, @NonNull String shapeName) {
        this.shapeId = shapeId;
        this.shapeName = shapeName;
    }

    @NonNull
    public String getShapeId() {
        return shapeId;
    }

    @NonNull
    public String getShapeName() {
        return shapeName;
    }
}
