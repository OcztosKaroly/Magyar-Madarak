package com.example.magyar_madarak;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(BaseActivity.this, KnowledgeBaseActivity.class));
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(BaseActivity.this, RegisterActivity.class));
                return true;
            }
            return false;
        });
    }
}