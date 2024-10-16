package com.example.magyar_madarak.utils;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtils {
    @TypeConverter
    public static String fromArrayListToString(ArrayList<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static ArrayList<String> fromStringToArrayList(String s) {
        if (s == null) {
            return null;
        }
        return new Gson().fromJson(s, new TypeToken<ArrayList<String>>() {}.getType());
    }
}
