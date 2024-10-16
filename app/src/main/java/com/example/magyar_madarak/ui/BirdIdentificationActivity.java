package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import static java.util.Collections.list;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BirdIdentificationActivity extends AppCompatActivity {
    private static final String LOG_TAG = BirdIdentificationActivity.class.getName();

    private Button nextBtn, previousBtn;

    private FragmentManager mFragmentManager;
    private Fragment colorFragment, shapeFragment, habitatFragment, resultFragment;
//    private ArrayList<String> selectedColors, selectedShapes, selectedHabitats;
    private Map<String, ArrayList<String>> selectedItems;
    private Fragment currentFragment;

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

        nextBtn = findViewById(R.id.btnNextBirdIdentification);
        nextBtn.setVisibility(View.VISIBLE);
        previousBtn = findViewById(R.id.btnPreviousBirdIdentification);
        previousBtn.setVisibility(View.INVISIBLE);

        colorFragment = BirdColorFragment.newInstance("colorFragment");
        shapeFragment = BirdShapeFragment.newInstance("shapeFragment");
        habitatFragment = BirdHabitatFragment.newInstance("habitatFragment");
        resultFragment = BirdIdentificationResultsFragment.newInstance();

        currentFragment = colorFragment;
        mFragmentManager = getSupportFragmentManager();
        setFragment(colorFragment, "colorFragment");

        selectedItems = new HashMap<>();
//        selectedColors = new ArrayList<>();
//        selectedShapes = new ArrayList<>();
//        selectedHabitats = new ArrayList<>();

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        nextBtn.setOnClickListener(v -> {
            if (currentFragment.getTag() != null) {
                switch (Objects.requireNonNull(currentFragment.getTag())) {
                    case "colorFragment":
                        setFragment(shapeFragment, "shapeFragment");
                        previousBtn.setVisibility(View.VISIBLE);
                        break;
                    case "shapeFragment":
                        setFragment(habitatFragment, "habitatFragment");
                        break;
                    case "habitatFragment":
                        setFragment(resultFragment, "resultFragment");
                        nextBtn.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        setFragment(colorFragment, "colorFragment");
                        break;
                }
            }
        });

        previousBtn.setOnClickListener(v -> {
            if (currentFragment.getTag() != null) {
                switch (currentFragment.getTag()) {
                    case "resultFragment":
                        setFragment(habitatFragment, "habitatFragment");
                        nextBtn.setVisibility(View.VISIBLE);
                        break;
                    case "habitatFragment":
                        setFragment(shapeFragment, "shapeFragment");
                        break;
                    case "shapeFragment":
                        setFragment(colorFragment, "colorFragment");
                        previousBtn.setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                Log.e(LOG_TAG, "--Current fragment tag is missing!--");
            }

        });
    }

    private void setFragment(Fragment replaceFragment, String replaceFragmentTag) {
        mFragmentManager.beginTransaction()
                .replace(mContent.getId(), replaceFragment, replaceFragmentTag)
                .commit();
        currentFragment = replaceFragment;
    }

    public void receiveSelectedListFromFragment(String fragmentTag, List<String> selectedList) {
        if (Arrays.asList("colorFragment", "shapeFragment", "habitatFragment").contains(fragmentTag)) {
            selectedItems.put(fragmentTag, (ArrayList<String>) selectedList);
        }
    }
}

