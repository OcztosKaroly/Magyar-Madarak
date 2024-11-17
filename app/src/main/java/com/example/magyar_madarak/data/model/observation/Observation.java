package com.example.magyar_madarak.data.model.observation;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "observations")
public class Observation {
    @PrimaryKey
    @NonNull
    private String observationId;
    @NonNull
    private String userId;

    @NonNull
    private Date creationDate;
    @NonNull
    private Date lastModificationDate;
    @NonNull
    private String name;
    private Date observationDate;
    private String description;

    public Observation(@NonNull String observationId,
                       @NonNull String userId,
                       @NonNull Date creationDate,
                       @NonNull Date lastModificationDate,
                       @NonNull String name,
                       Date observationDate,
                       String description) {
        this.observationId = observationId;
        this.userId = userId;

        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
        this.name = name;
        this.observationDate = observationDate;
        this.description = description;
    }

    @NonNull
    public String getObservationId() {
        return observationId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Date getCreationDate() {
        return creationDate;
    }

    @NonNull
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(@NonNull Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Observation{" +
                ", name='" + name + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                ", observationDate='" + observationDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Observation other = (Observation) obj;
        return this.getObservationId().equals(other.getObservationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getObservationId());
    }
}
