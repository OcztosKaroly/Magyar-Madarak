package com.example.magyar_madarak.utils;

import androidx.room.TypeConverter;

import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Diet;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class ConverterUtils {

//    @TypeConverter
//    public static String fromArrayListToString(ArrayList<String> list) {
//        return new Gson().toJson(list);
//    }
//
//    @TypeConverter
//    public static ArrayList<String> fromStringToArrayList(String s) {
//        return new Gson().fromJson(s, new TypeToken<ArrayList<String>>() {}.getType());
//    }


    @TypeConverter
    public static String fromListToString(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static List<String> toListFromString(String s) {
        return new Gson().fromJson(s, new TypeToken<List<String>>() {}.getType());
    }


    @TypeConverter
    public static String fromDietListToString(List<Diet> diets) {
        return new Gson().toJson(diets);
    }

    @TypeConverter
    public static List<Diet> toDietListFromString(String diets) {
        return new Gson().fromJson(diets, new TypeToken<List<Diet>>() {}.getType());
    }


    @TypeConverter
    public static String fromColorListToString(List<Color> colors) {
        return new Gson().toJson(colors);
    }

    @TypeConverter
    public static List<Color> toColorListFromString(String colors) {
        return new Gson().fromJson(colors, new TypeToken<List<Color>>() {}.getType());
    }


    @TypeConverter
    public static String fromShapeListToString(List<Shape> shapes) {
        return new Gson().toJson(shapes);
    }

    @TypeConverter
    public static List<Shape> toShapeListFromString(String shapes) {
        return new Gson().fromJson(shapes, new TypeToken<List<Shape>>() {}.getType());
    }


    @TypeConverter
    public static String fromHabitatListToString(List<Habitat> habitats) {
        return new Gson().toJson(habitats);
    }

    @TypeConverter
    public static List<Habitat> toHabitatListFromString(String habitats) {
        return new Gson().fromJson(habitats, new TypeToken<List<Habitat>>() {}.getType());
    }


    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
