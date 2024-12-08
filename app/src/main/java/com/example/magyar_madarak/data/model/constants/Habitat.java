package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "habitats")
public class Habitat {
    @PrimaryKey
    @NonNull
    private String habitatId;
    @NonNull
    private String habitatName;

    public Habitat(@NonNull String habitatId, @NonNull String habitatName) {
        this.habitatId = habitatId;
        this.habitatName = habitatName;
    }

    @NonNull
    public String getHabitatId() { return habitatId; }

    @NonNull
    public String getHabitatName() {
        return habitatName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return Objects.equals(this.getHabitatId(), ((Habitat) other).getHabitatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHabitatId());
    }
}
