package com.example.magyar_madarak.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.magyar_madarak.ui.BirdIdentificationActivity;
import com.example.magyar_madarak.ui.KnowledgeBaseActivity;
import com.example.magyar_madarak.ui.LoginActivity;
import com.example.magyar_madarak.R;
import com.example.magyar_madarak.ui.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationUtils {
    private static final String LOG_TAG = "NAVIGATION";

    public static void startActivity(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        Log.i(LOG_TAG, "--Start activity from " + from.getClass().getSimpleName().toUpperCase() + " to " + to.getSimpleName().toUpperCase() + ".--");

        from.startActivity(intent);
    }

    public static void redirect(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        Log.i(LOG_TAG, "--Redirecting from " + from.getClass().getSimpleName().toUpperCase() + " to " + to.getSimpleName().toUpperCase() + ".--");

        from.startActivity(intent);

        if (from instanceof Activity) {
            Log.i(LOG_TAG, "--Finish activity " + from.getClass().getSimpleName().toUpperCase() + ".--");
            ((Activity) from).finish();
        }
    }

    public static void navigationBarRedirection(BottomNavigationView bottomNavigationView, Context from) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home && !from.getClass().equals(KnowledgeBaseActivity.class)) {
                redirect(from, KnowledgeBaseActivity.class);
                return true;
            }
            if (itemId == R.id.nav_bird_identification && !from.getClass().equals(BirdIdentificationActivity.class)) {
                redirect(from, BirdIdentificationActivity.class);
                return true;
            }
//            if (itemId == R.id.nav_observations && !from.getClass().equals(Observations.class)) {
            if (itemId == R.id.nav_observations) {
                redirect(from, LoginActivity.class);
                return true;
            }
            if (itemId == R.id.nav_profile && !from.getClass().equals(LoginActivity.class)) {
                redirect(from, LoginActivity.class);
                return true;
            }

            return false;
        });
    }
}
