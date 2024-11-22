package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments.BirdColorFragment;
import com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments.BirdHabitatFragment;
import com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments.BirdIdentificationResultsFragment;
import com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments.BirdShapeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BirdIdentificationActivity extends AppCompatActivity {
    private static final String LOG_TAG = BirdIdentificationActivity.class.getName();

    private SharedPreferences mSharedPreferences;

    private Button nextBtn, previousBtn;

    private FragmentManager mFragmentManager;
    private Fragment colorFragment, shapeFragment, habitatFragment, resultFragment;
    private String currentFragmentClassName;

    private View mView;
    private FrameLayout mContent;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bird_identification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.main);
        mContent = findViewById(R.id.contentBirdIdentification);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.nav_bird_identification).setChecked(true);

        mSharedPreferences = this.getSharedPreferences("birdIdentification", MODE_PRIVATE);
        mSharedPreferences.edit().clear().apply();

        nextBtn = findViewById(R.id.btnNextBirdIdentification);
        nextBtn.setVisibility(View.VISIBLE);
        previousBtn = findViewById(R.id.btnPreviousBirdIdentification);
        previousBtn.setVisibility(View.INVISIBLE);

        colorFragment = BirdColorFragment.newInstance();
        shapeFragment = BirdShapeFragment.newInstance();
        habitatFragment = BirdHabitatFragment.newInstance();
        resultFragment = BirdIdentificationResultsFragment.newInstance();

        mFragmentManager = getSupportFragmentManager();
        setFragment(colorFragment);

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        nextBtn.setOnClickListener(v -> {
            if (currentFragmentClassName != null) {
                switch (currentFragmentClassName) {
                    case "BirdColorFragment":
                        setFragment(shapeFragment);
                        previousBtn.setVisibility(View.VISIBLE);
                        break;
                    case "BirdShapeFragment":
                        setFragment(habitatFragment);
                        break;
                    case "BirdHabitatFragment":
                        setFragment(resultFragment);
                        nextBtn.setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                Log.w(LOG_TAG, "--Current fragment is missing!--");
            }
        });

        previousBtn.setOnClickListener(v -> {
            if (currentFragmentClassName != null) {
                switch (currentFragmentClassName) {
                    case "BirdIdentificationResultsFragment":
                        setFragment(habitatFragment);
                        nextBtn.setVisibility(View.VISIBLE);
                        break;
                    case "BirdHabitatFragment":
                        setFragment(shapeFragment);
                        break;
                    case "BirdShapeFragment":
                        setFragment(colorFragment);
                        previousBtn.setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                Log.w(LOG_TAG, "--Current fragment is missing!--");
            }
        });
    }

    private void setFragment(Fragment replaceFragment) {
        mFragmentManager.beginTransaction()
                .replace(mContent.getId(), replaceFragment)
                .commit();
        currentFragmentClassName = replaceFragment.getClass().getSimpleName();
    }
}

