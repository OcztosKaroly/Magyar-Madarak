package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
