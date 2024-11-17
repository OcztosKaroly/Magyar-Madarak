package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

import java.util.ArrayList;
import java.util.List;

public class ObservationsActivity extends AppCompatActivity {
    private static final String LOG_TAG = ObservationsActivity.class.getName();

    FloatingActionButton createObservationBtn;

    private RecyclerView mRecyclerView;
    private ObservationsAdapter mObservationsAdapter;
    private LiveData<List<Observation>> mObservations;

    private ObservationViewModel mObservationViewModel;

    private View mView;
    private BottomNavigationView mBottomNavigationView;

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

        createObservationBtn.setOnClickListener(l -> {
            NavigationUtils.startActivity(this, ObservationPageActivity.class);
        });

        mObservations.observe(this, observations -> {
            mObservationsAdapter.setObservations(observations);
        });
    }

    private LiveData<List<Observation>> loadObservations() {
        return mObservationViewModel.getAllObservations();
    }
}