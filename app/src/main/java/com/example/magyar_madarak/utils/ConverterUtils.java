package com.example.magyar_madarak.utils;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ConverterUtils {
    @TypeConverter
    public String fromArrayListToString(ArrayList<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public ArrayList<String> fromStringToArrayList(String s) {
        return new Gson().fromJson(s, new TypeToken<ArrayList<String>>() {}.getType());
    }
}
