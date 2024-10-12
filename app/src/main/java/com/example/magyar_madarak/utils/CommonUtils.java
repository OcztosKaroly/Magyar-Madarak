package com.example.magyar_madarak.utils;

public class CommonUtils {
    public static String capitalizeFirstLetter(String string) {
        return Character.toTitleCase(string.charAt(0)) + string.substring(1);
    }
}
