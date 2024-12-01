package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.AuthUtils.isUserAuthenticated;
import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;
import static com.example.magyar_madarak.utils.ResourceAvailabilityUtils.isWifiConnected;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.observation.Observation;
import com.example.magyar_madarak.data.viewModel.ObservationViewModel;
import com.example.magyar_madarak.ui.Adapters.ObservationsAdapter;
import com.example.magyar_madarak.ui.Pages.ObservationPageActivity;
import com.example.magyar_madarak.utils.NavigationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObservationsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ObservationsActivity.class.getName();

    FloatingActionButton createObservationBtn;

    private RecyclerView mRecyclerView;
    private ObservationsAdapter mObservationsAdapter;
    private LiveData<List<Observation>> mObservations;

    private ObservationViewModel mObservationViewModel;

    private TextView notLoggedInTW, offlineTW, userSettingsTW;

    private View mView;
    private BottomNavigationView mBottomNavigationView;

    private boolean wasAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_observations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentObservations);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.nav_observations).setChecked(true);

        mObservationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);

        setTopUserBar();

        mObservationsAdapter = new ObservationsAdapter(this, new ArrayList<>());
        mRecyclerView = findViewById(R.id.recyclerViewObservations);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mObservationsAdapter);
        mObservations = loadObservations();

        createObservationBtn = findViewById(R.id.btnCreateObservation);

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        createObservationBtn.setOnClickListener(l -> NavigationUtils.startActivity(this, ObservationPageActivity.class));

        mObservations.observe(this, observations -> mObservationsAdapter.setObservations(observations));

        notLoggedInTW.setOnClickListener(l -> NavigationUtils.startActivity(this, LoginActivity.class));
        offlineTW.setOnClickListener(l -> NavigationUtils.startActivity(this, Settings.class));
    }

    private void setTopUserBar() {
        notLoggedInTW = findViewById(R.id.textViewNotLoggedIn);
        offlineTW = findViewById(R.id.textViewOffline);
        userSettingsTW = findViewById(R.id.textViewUserSettings);

        notLoggedInTW.setVisibility(View.GONE);
        offlineTW.setVisibility(View.GONE);
        userSettingsTW.setVisibility(View.GONE);

        if (isWifiConnected(this)) {
            if (isUserAuthenticated()) {
                userSettingsTW.setVisibility(View.VISIBLE);
                String userSettingText = "Be vagy jelentkezve (" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() + ").";
                userSettingsTW.setText(userSettingText);
                wasAuthenticated = true;
            } else {
                notLoggedInTW.setVisibility(View.VISIBLE);
                wasAuthenticated = false;
            }
        } else {
            offlineTW.setVisibility(View.VISIBLE);
            if (isUserAuthenticated()) {
                wasAuthenticated = true;
            }
        }
    }

    private LiveData<List<Observation>> loadObservations() {
        return mObservationViewModel.getAllObservations();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (wasAuthenticated != isUserAuthenticated()) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "--Error at reloading Observations.--", e);
        }
    }
}