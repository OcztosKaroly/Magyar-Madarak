package com.example.magyar_madarak.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "observations")
public class Observation {
    @PrimaryKey
    @NonNull
    private String observationId;
    private String userId;

    private String name;
    private String creationDate; // TODO: creationDate adattag típusát Date-ra váltani?
    private String lastModificationDate; // TODO: lastModificationDate adattag típusát Date-ra váltani?
    private String location;
    private String date; // TODO: date adattag típusát Date-ra váltani?
    private List<String> observedBirdColors;
    private List<String> observedBirdShapes;
    private List<String> observedBirdHabitats;
    private List<String> possibleBirds; // TODO: String helyett Bird adattípus?
    private String description;

    public Observation() {
        observationId = "";
    }

    public Observation(@NonNull String id,
                       String userId,
                       String name,
                       String creationDate,
                       String lastModificationDate,
                       String location,
                       String date,
                       List<String> observedBirdColors,
                       List<String> observedBirdShapes,
                       List<String> observedBirdHabitats,
                       List<String> possibleBirds,
                       String description) {
        this.observationId = id;
        this.userId = userId;

        this.name = name;
        this.creationDate = creationDate;
        this.lastModificationDate = lastModificationDate;
        this.location = location;
        this.date = date;
        this.observedBirdColors = observedBirdColors;
        this.observedBirdShapes = observedBirdShapes;
        this.observedBirdHabitats = observedBirdHabitats;
        this.possibleBirds = possibleBirds;
        this.description = description;
    }

    @NonNull
    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(@NonNull String id) {
        this.observationId = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(String lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getObservedBirdColors() {
        return observedBirdColors;
    }

    public void setObservedBirdColors(List<String> observedBirdColors) {
        this.observedBirdColors = observedBirdColors;
    }

    public List<String> getObservedBirdShapes() {
        return observedBirdShapes;
    }

    public void setObservedBirdShapes(List<String> observedBirdShapes) {
        this.observedBirdShapes = observedBirdShapes;
    }

    public List<String> getObservedBirdHabitats() {
        return observedBirdHabitats;
    }

    public void setObservedBirdHabitats(List<String> observedBirdHabitats) {
        this.observedBirdHabitats = observedBirdHabitats;
    }

    public List<String> getPossibleBirds() {
        return possibleBirds;
    }

    public void setPossibleBirds(List<String> possibleBirds) {
        this.possibleBirds = possibleBirds;
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
                "id='" + observationId + '\'' +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                ", location='" + location + '\'' +
                ", observedBirdColors=" + observedBirdColors +
                ", observedBirdShapes=" + observedBirdShapes +
                ", observedBirdHabitats=" + observedBirdHabitats +
                ", possibleBirds=" + possibleBirds +
                ", description='" + description + '\'' +
                '}';
    }
}
