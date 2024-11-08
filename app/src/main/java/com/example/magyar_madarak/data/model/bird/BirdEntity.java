package com.example.magyar_madarak.data.model.bird;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.magyar_madarak.data.model.constants.ConservationValue;

import java.util.List;

@Entity(
        tableName = "birds",
        foreignKeys = @ForeignKey(
                entity = ConservationValue.class,
                parentColumns = "conservationId",
                childColumns = "conservationId"
        ),
        indices = {
                @Index("conservationId")
        }
)
public class BirdEntity {
    @PrimaryKey
    @NonNull
    private String birdId;
    @NonNull
    private String birdName;
    private String mainPictureId;

    private String latinName;
    private boolean migratory;
    private String size;
    private String wingSpan;
    private String conservationId;

    private String description;
    private List<String> facts;

    private List<String> pictureIds;

    public BirdEntity(@NonNull String birdId,
                      @NonNull String birdName,
                      String mainPictureId,
                      String latinName,
                      boolean migratory,
                      String size,
                      String wingSpan,
                      String conservationId,
                      String description,
                      List<String> facts,
                      List<String> pictureIds) {
        this.birdId = birdId;
        this.birdName = birdName;
        this.mainPictureId = mainPictureId;
        this.latinName = latinName;
        this.migratory = migratory;
        this.size = size;
        this.wingSpan = wingSpan;
        this.conservationId = conservationId;
        this.description = description;
        this.facts = facts;
        this.pictureIds = pictureIds;
    }

    @NonNull
    public String getBirdId() {
        return birdId;
    }

    @NonNull
    public String getBirdName() {
        return birdName;
    }

    public String getMainPictureId() {
        return mainPictureId;
    }

    public String getLatinName() {
        return latinName;
    }

    public boolean isMigratory() {
        return migratory;
    }

    public String getSize() {
        return size;
    }

    public String getWingSpan() {
        return wingSpan;
    }

    public String getConservationId() {
        return conservationId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFacts() {
        return facts;
    }

    public List<String> getPictureIds() {
        return pictureIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "birdName='" + birdName + '\'' +
                ", mainPictureId='" + mainPictureId + '\'' +
                ", latinName='" + latinName + '\'' +
                ", migratory=" + migratory +
                ", size='" + size + '\'' +
                ", wingSpan='" + wingSpan + '\'' +
                ", conservationId='" + conservationId + '\'' +
                ", description='" + description + '\'' +
                ", facts=" + facts +
                ", pictureIds=" + pictureIds;
    }
}
