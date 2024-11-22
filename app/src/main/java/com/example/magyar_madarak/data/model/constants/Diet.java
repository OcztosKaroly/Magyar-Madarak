package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "diets")
public class Diet {
    @PrimaryKey
    @NonNull
    private String dietId;
    @NonNull
    private String dietName;

    public Diet(@NonNull String dietId, @NonNull String dietName) {
        this.dietId = dietId;
        this.dietName = dietName;
    }

    @NonNull
    public String getDietId() {
        return dietId;
    }

    @NonNull
    public String getDietName() {
        return dietName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Objects.equals(this.getDietId(), ((Diet) other).getDietId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDietId());
    }
}
