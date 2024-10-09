package com.example.magyar_madarak;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contentMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeData();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    private void initializeData() {
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

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
    }
}