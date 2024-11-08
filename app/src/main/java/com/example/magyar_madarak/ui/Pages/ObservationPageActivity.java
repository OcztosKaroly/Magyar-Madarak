package com.example.magyar_madarak.ui.Pages;

import static com.example.magyar_madarak.ui.Adapters.ObservationsAdapter.selectedObservation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.ui.LoginActivity;
import com.example.magyar_madarak.ui.ObservationsActivity;

public class ObservationPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = ObservationsActivity.class.getName();

    private Button deleteBtn, saveBtn;
    private EditText observationNameET, observationDateET, observationLocationET, observationDescriptionET;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_observation_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (selectedObservation != null) {
            Log.i(LOG_TAG, "--Modify observation " + selectedObservation.getName() + ".--");
        } else {
            Log.i(LOG_TAG, "--Start creating new observation.--");
        }

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentObservationPage);

        deleteBtn = findViewById(R.id.btnDeleteObservation);
        saveBtn = findViewById(R.id.btnSaveObservation);

        observationNameET = findViewById(R.id.etObservationName);
        observationDateET = findViewById(R.id.etObservationDate);
        observationLocationET = findViewById(R.id.etObservationLocation);
        observationDescriptionET = findViewById(R.id.etObservationDescription);

        initializeListeners();
    }

    private void initializeListeners() {
        deleteBtn.setOnClickListener(v -> deleteObservation());
        saveBtn.setOnClickListener(v -> saveObservation());
    }

    private void deleteObservation() {
        // TODO: Ha létezett eddig az adatbázisban, akkor törölni onnan, különben csak simán semmi.
        selectedObservation = null;
        finish();
    }

    private void saveObservation() {
        String observationName = String.valueOf(observationNameET.getText()).trim();
        if (observationName.isEmpty()) {
            Toast.makeText(this, "Kötelező nevet adni a megfigyelésnek!", Toast.LENGTH_LONG).show();
        } else {
            String observationDate = String.valueOf(observationDateET.getText()).trim();
            String observationLocation = String.valueOf(observationLocationET.getText()).trim();
            String observationDescription = String.valueOf(observationDescriptionET.getText()).trim();
            // TODO: Ha létezett eddig az adatbázisban, akkor módosítani azt, különben létrehozni és beszúrni.
            selectedObservation = null;
            finish();
        }
    }


}