package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Objects.equals(this.getShapeId(), ((Shape) other).getShapeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShapeId());
    }
}
