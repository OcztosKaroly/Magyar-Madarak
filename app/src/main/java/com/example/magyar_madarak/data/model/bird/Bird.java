package com.example.magyar_madarak.data.model.bird;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.magyar_madarak.data.model.CrossRefTables.ColorCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.DietCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.HabitatCrossRef;
import com.example.magyar_madarak.data.model.CrossRefTables.ShapeCrossRef;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.ConservationValue;
import com.example.magyar_madarak.data.model.constants.Diet;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;

import java.util.List;
import java.util.Objects;

public class Bird {
    @Embedded
    private BirdEntity bird;

    @Relation(
            parentColumn = "conservationId",
            entityColumn = "conservationId"
    )
    private ConservationValue conservationValue;

    @Relation(
            parentColumn = "birdId",
            entityColumn = "dietId",
            associateBy = @Junction(DietCrossRef.class)
    )
    private List<Diet> diets;

    @Relation(
            parentColumn = "birdId",
            entityColumn = "colorId",
            associateBy = @Junction(ColorCrossRef.class)
    )
    private List<Color> colors;

    @Relation(
            parentColumn = "birdId",
            entityColumn = "shapeId",
            associateBy = @Junction(ShapeCrossRef.class)
    )
    private List<Shape> shapes;

    @Relation(
            parentColumn = "birdId",
            entityColumn = "habitatId",
            associateBy = @Junction(HabitatCrossRef.class)
    )
    private List<Habitat> habitats;

    public Bird(BirdEntity bird) {
        this.bird = bird;
    }

    public BirdEntity getBird() {
        return bird;
    }

    @NonNull
    public String getBirdId() {
        return bird.getBirdId();
    }

    @NonNull
    public String getBirdName() {
        return bird.getBirdName();
    }

    public String getMainPictureId() {
        return bird.getMainPictureId();
    }

    public String getLatinName() {
        return bird.getLatinName();
    }

    public boolean isMigratory() {
        return bird.isMigratory();
    }

    public String getSize() {
        return bird.getSize();
    }

    public String getWingSpan() {
        return bird.getWingSpan();
    }

    public String getDescription() {
        return bird.getDescription();
    }

    public List<String> getFacts() {
        return bird.getFacts();
    }

    public List<String> getPictureIds() {
        return bird.getPictureIds();
    }

    public ConservationValue getConservationValue() {
        return conservationValue;
    }

    public String getConservation() {
        return conservationValue.getConservation();
    }

    public void setConservationValue(ConservationValue conservationValue) {
        if (this.conservationValue == null) {
            this.conservationValue = conservationValue;
        }
    }

    public List<Diet> getDiets() {
        return diets;
    }

    public void setDiets(List<Diet> diets) {
        if (this.diets == null) {
            this.diets = diets;
        }
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        if (this.colors == null) {
            this.colors = colors;
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        if (this.shapes == null) {
            this.shapes = shapes;
        }
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }

    public void setHabitats(List<Habitat> habitats) {
        if (this.habitats == null) {
            this.habitats = habitats;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Bird{" +
                bird.toString() +
                ", conservation=" + conservationValue.getConservation() +
                ", diets=" + diets +
                ", colors=" + colors +
                ", shapes=" + shapes +
                ", habitats=" + habitats +
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
        Bird other = (Bird) obj;
        return this.getBirdId().equals(other.getBirdId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBirdId());
    }
}

