package com.example.magyar_madarak.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.magyar_madarak.KnowledgeBaseActivity;
import com.example.magyar_madarak.LoginActivity;
import com.example.magyar_madarak.MainActivity;
import com.example.magyar_madarak.R;
import com.example.magyar_madarak.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationUtils {
    private static final String LOG_TAG = "NAVIGATION";

    public static void redirect(Context from, Class<?> to) {
        Intent intent = new Intent(from, to);
        Log.i(LOG_TAG, "--Redirecting from " + from.getClass().getSimpleName().toUpperCase() + " to " + to.getSimpleName().toUpperCase() + ".--");
        from.startActivity(intent);
    }

    public static void navigationBarRedirection(BottomNavigationView bottomNavigationView, Context from) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            item.setChecked(true);

            if (itemId == R.id.nav_home && !from.getClass().equals(KnowledgeBaseActivity.class)) {
                redirect(from, KnowledgeBaseActivity.class);
                return true;
            }
            if (itemId == R.id.nav_search && !from.getClass().equals(LoginActivity.class)) {
                redirect(from, LoginActivity.class);
                return true;
            }
            if (itemId == R.id.nav_profile && !from.getClass().equals(RegisterActivity.class)) {
                redirect(from, RegisterActivity.class);
                return true;
            }

            return false;
        });
    }
}
