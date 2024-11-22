package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "colors")
public class Color {
    @PrimaryKey
    @NonNull
    private String colorId;
    @NonNull
    private String colorName;

    public Color(@NonNull String colorId, @NonNull String colorName) {
        this.colorId = colorId;
        this.colorName = colorName;
    }

    @NonNull
    public String getColorId() {
        return colorId;
    }

    @NonNull
    public String getColorName() {
        return colorName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Objects.equals(this.getColorId(), ((Color) other).getColorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColorId());
    }
}
