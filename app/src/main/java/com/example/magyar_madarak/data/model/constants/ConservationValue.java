package com.example.magyar_madarak.data.model.constants;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conservation_values")
public class ConservationValue {
    @PrimaryKey
    @NonNull
    private String conservationId;
    private int conservationValue;

    public ConservationValue(@NonNull String conservationId, int conservationValue) {
        this.conservationId = conservationId;
        this.conservationValue = conservationValue;
    }

    @NonNull
    public String getConservationId() {
        return conservationId;
    }

    public int getConservationValue() {
        return conservationValue;
    }

    public String getConservation() {
        return conservationValue == 0 ? "nem védett" : conservationValue + " Ft";
    }
}
