package com.example.magyar_madarak;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        initializeData();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(MainActivity.this, KnowledgeBaseActivity.class));
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                return true;
            }
            return false;
        });
    }

//    private void initializeData() {
//        mBottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        initializeListeners();
//    }
//
//    private void initializeListeners() {
//        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.nav_home) {
//                startActivity(new Intent(MainActivity.this, KnowledgeBaseActivity.class));
//                return true;
//            } else if (itemId == R.id.nav_search) {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                return true;
//            } else if (itemId == R.id.nav_profile) {
//                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//                return true;
//            }
//            return false;
//        });
//    }
}