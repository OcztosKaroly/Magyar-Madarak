package com.example.magyar_madarak.ui.Pages;

import static com.example.magyar_madarak.ui.Adapters.ObservationsAdapter.selectedObservation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Observation;
import com.google.android.material.appbar.MaterialToolbar;

public class CreateObservationPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = CreateObservationPageActivity.class.getName();

//    private MaterialToolbar toolbar;

    private Button exitBtn;

//    private Observation mObservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_observation_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        if (selectedObservation == null) {
//            Log.w(LOG_TAG, "--No observation selected!--");
//            finish();
//            return;
//        }

        initializeData();
    }

    private void initializeData() {
//        mObservation = selectedObservation;
//
//        toolbar = findViewById(R.id.toolbarObservationPage);

        exitBtn = findViewById(R.id.btn_exit);

        initializeListeners();
    }

    private void initializeListeners() {
        exitBtn.setOnClickListener(l -> {
            Log.i(LOG_TAG, "--Exit observation page.--");
            finish();
        });

//        toolbar.setNavigationOnClickListener(v -> {
//            Log.i(LOG_TAG, "--Navigate back.--");
//            finish();
//        });
//
//        try {
//            TextView title = (TextView) toolbar.getChildAt(0);
//            title.setOnClickListener(v -> {
//                finish();
//                Log.i(LOG_TAG, "--Navigate back.--");
//            });
//        } catch (Exception e) {
//            Log.e(LOG_TAG, "--Error in set onclick listener on toolbar title.--", e);
//        }
    }
}