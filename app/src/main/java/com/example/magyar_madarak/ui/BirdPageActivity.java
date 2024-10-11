package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.BirdKBAdapter.selectedBird;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Bird;

public class BirdPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = BirdPageActivity.class.getName();

    private Bird mBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bird_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        if (!(selectedBird instanceof Bird)) {
            selectedBird = null;
            Log.w(LOG_TAG, "--No bird selected.--");
            finish();
            return;
        }

        initializeData();
    }

    private void initializeData() {
        mBird = selectedBird;

        initializeListeners();
    }

    private void initializeListeners() { }
}