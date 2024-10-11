package com.example.magyar_madarak.data.model;

import android.graphics.drawable.Icon;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "birds")
public class Bird {
    @PrimaryKey
    @NonNull
    private String id;

    private String name;
    private String latinName;
    private String size;
    private String wingSpan;
    private ArrayList<String> shapes;
    private ArrayList<String> colors;
    private boolean migratory;
    private int conservationValue;

    private String description;
    private ArrayList<String> facts;

    public Bird() {
        id = "";
    }

    public Bird(@NonNull String id,
                String name,
                String latinName,
                String size,
                String wingSpan,
                ArrayList<String> shapes,
                ArrayList<String> colors,
                boolean migratory,
                int conservationValue,
                String description,
                ArrayList<String> facts) {
        this.id = id;
        this.name = name;
        this.latinName = latinName;
        this.size = size;
        this.wingSpan = wingSpan;
        this.shapes = shapes;
        this.colors = colors;
        this.migratory = migratory;
        this.conservationValue = conservationValue;
        this.description = description;
        this.facts = facts;
    }

//    public Icon getImage() {
//        return; // TODO: a madárról egy kicsi kép visszaadása. Legjobb az lenne, ha a nagy képeket le tudnánk kicsinyíteni ikon méretre
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWingSpan() {
        return wingSpan;
    }

    public void setWingSpan(String wingSpan) {
        this.wingSpan = wingSpan;
    }

    public ArrayList<String> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<String> shapes) {
        this.shapes = shapes;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public boolean isMigratory() {
        return migratory;
    }

    public void setMigratory(boolean migratory) {
        this.migratory = migratory;
    }

    public int getConservationValue() {
        return conservationValue;
    }

    public void setConservationValue(int conservationValue) {
        this.conservationValue = conservationValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getFacts() {
        return facts;
    }

    public void setFacts(ArrayList<String> facts) {
        this.facts = facts;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latinName='" + latinName + '\'' +
                ", size=" + size +
                ", wingSpan=" + wingSpan +
                ", shapes=" + shapes +
                ", colors=" + colors +
                ", migratory=" + migratory +
                ", conservationValue=" + conservationValue +
                ", description='" + description + '\'' +
                ", facts=" + facts +
                '}';
    }
}
