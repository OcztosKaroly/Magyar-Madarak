package com.example.magyar_madarak.data.model;

import android.graphics.drawable.Icon;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "birds")
public class Bird {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    private String latinName;
    private int size;
    private ArrayList<String> bodyShapes; // testalkat, vannak pl varjú-galamb testalkatú madarak
    private ArrayList<String> colors;
    private boolean migratory;
    private int conservationValue;

    public String description;
    public String facts;

    public Bird(@NonNull String name,
                String latinName,
                int size,
                ArrayList<String> bodyShapes,
                ArrayList<String> colors,
                boolean migratory,
                int conservationValue,
                String description,
                String facts) {
        this.name = name;
        this.latinName = latinName;
        this.size = size;
        this.bodyShapes = bodyShapes;
        this.colors = colors;
        this.migratory = migratory;
        this.conservationValue = conservationValue;
        this.description = description;
        this.facts = facts;
    }

//    public Icon getImage() {
//        return; // TODO: a madárról egy kicsi kép visszaadása. Legjobb az lenne, ha a nagy képeket le tudnánk kicsinyíteni ikon méretre
//    }


    @NonNull
    public String getName() { return name; }

    public String getLatinName() { return latinName; }

    public int getSize() { return size; }

    public ArrayList<String> getBodyShapes() { return bodyShapes; }

    public ArrayList<String> getColors() { return colors; }

    public boolean isMigratory() { return migratory; }

    public int getConservationValue() { return conservationValue; }

    public String getDescription() { return description; }

    public String getFacts() { return facts; }
}
