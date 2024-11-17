package com.example.magyar_madarak.ui.Pages;

import static com.example.magyar_madarak.ui.Adapters.ObservationsAdapter.selectedObservation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.viewModel.ObservationViewModel;
import com.example.magyar_madarak.ui.LoginActivity;
import com.example.magyar_madarak.ui.ObservationsActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Calendar;
import java.util.Date;

public class ObservationPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = ObservationsActivity.class.getName();

    private MaterialToolbar toolbar;

    private Button deleteBtn, saveBtn;
    private EditText observationNameET, observationDescriptionET;
    private DatePicker observationDateDP;
    private TimePicker observationDateTP;

    private ObservationViewModel mObservationViewModel;
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

        mObservationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);

        toolbar = findViewById(R.id.toolbarObservationPage);

        deleteBtn = findViewById(R.id.btnDeleteObservation);
        saveBtn = findViewById(R.id.btnSaveObservation);

        observationNameET = findViewById(R.id.etObservationName);
        observationDescriptionET = findViewById(R.id.etObservationDescription);
        observationDateDP = findViewById(R.id.datePickerObservationDate);
        observationDateTP = findViewById(R.id.timePickerObservationDate);

        initializeListeners();
    }

    private void initializeListeners() {
        deleteBtn.setOnClickListener(v -> deleteObservation());
        saveBtn.setOnClickListener(v -> saveObservation());

        toolbar.setNavigationOnClickListener(v -> {
            Log.i(LOG_TAG, "--Navigate back.--");
            finish();
        });

        try {
            TextView title = (TextView) toolbar.getChildAt(0);
            title.setOnClickListener(v -> {
                finish();
                Log.i(LOG_TAG, "--Navigate back.--");
            });
        } catch (Exception e) {
            Log.e(LOG_TAG, "--Error in set onclick listener on toolbar title.--", e);
        }
    }

    private void deleteObservation() {
        if (selectedObservation != null) {
            mObservationViewModel.deleteObservation(selectedObservation);
            selectedObservation = null;
        }
        finish();
    }

    private void saveObservation() {
        String observationName = String.valueOf(observationNameET.getText()).trim();

        if (observationName.isEmpty()) {
            Toast.makeText(this, "Kötelező nevet adni a megfigyelésnek!", Toast.LENGTH_LONG).show();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(
                    observationDateDP.getYear(), observationDateDP.getMonth(), observationDateDP.getDayOfMonth(),
                    observationDateTP.getHour(), observationDateTP.getMinute(), 0
            );
            Date observationDate = calendar.getTime();

            String description = String.valueOf(observationDescriptionET.getText()).trim();

            if (selectedObservation == null) {
                mObservationViewModel.createObservation(observationName, observationDate, description);
            } else {
                selectedObservation.setName(observationName);
                selectedObservation.setObservationDate(observationDate);
                selectedObservation.setDescription(description);
                mObservationViewModel.updateObservation(selectedObservation);
                selectedObservation = null;
            }

            finish();
        }
    }
}
