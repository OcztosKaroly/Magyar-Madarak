package com.example.magyar_madarak.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.magyar_madarak.LoginActivity;
import com.example.magyar_madarak.RegisterActivity;

public class NavigationUtils {
    private static final String LOG_TAG = "NAVIGATION";

    public static void redirect(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        Log.i(LOG_TAG, "--Redirecting from " + from.getClass().getSimpleName().toUpperCase() + " to " + to.getSimpleName().toUpperCase() + ".--");
        from.startActivity(intent);
    }
}
