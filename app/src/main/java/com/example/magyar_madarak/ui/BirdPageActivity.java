package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.BirdKBAdapter.selectedBird;
import static com.example.magyar_madarak.utils.CommonUtils.capitalizeFirstLetter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Bird;
import com.google.android.material.appbar.MaterialToolbar;

public class BirdPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = BirdPageActivity.class.getName();

    private MaterialToolbar toolbar;

    private TextView tBirdName, tBirdLatinName, tBirdDiet, tBirdMigratory, tBirdSize, tBirdWingSpan, tBirdConservationValue,
                    tBirdDescription,
                    tBirdColors, tBirdShapes, tBirdHabitats,
                    tBirdFacts;
    private ImageView imageBird;

    private Bird mBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bird_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!(selectedBird instanceof Bird)) {
            Log.w(LOG_TAG, "--No bird selected!--");
            finish();
            return;
        }

        initializeData();
    }

    private void initializeData() {
        mBird = selectedBird;

        toolbar = findViewById(R.id.toolbarBirdPage);

        imageBird = findViewById(R.id.imageBird);
        tBirdName = findViewById(R.id.tBirdName);
        tBirdLatinName = findViewById(R.id.tBirdLatinName);
        tBirdDiet = findViewById(R.id.tBirdDiet);
        tBirdMigratory = findViewById(R.id.tBirdMigratory);
        tBirdSize = findViewById(R.id.tBirdSize);
        tBirdWingSpan = findViewById(R.id.tBirdWingSpan);
        tBirdConservationValue = findViewById(R.id.tBirdConservationValue);
        tBirdDescription = findViewById(R.id.tBirdDescription);
        tBirdColors = findViewById(R.id.tBirdColors);
        tBirdShapes = findViewById(R.id.tBirdShapes);
        tBirdHabitats = findViewById(R.id.tBirdHabitats);
        tBirdFacts = findViewById(R.id.tBirdFacts);
        setTexts();

        initializeListeners();
    }

    private void initializeListeners() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        try {
            TextView title = (TextView) toolbar.getChildAt(0);
            title.setOnClickListener(v -> {
                finish();
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, "--Error in set onclick listener on toolbar title: ", e);
        }
    }

    private void setTexts() {
        tBirdName.setText(capitalizeFirstLetter(mBird.getName()));
        tBirdLatinName.setText(mBird.getLatinName());
//        tBirdDiet.setText(mbird.getD);
        tBirdMigratory.setText(mBird.isMigratory() ? "Költöző" : "Itthon telelő");
        tBirdSize.setText(mBird.getSize());
        tBirdWingSpan.setText(mBird.getWingSpan());

        tBirdConservationValue.setText(mBird.getConservation());
        tBirdDescription.setText(mBird.getDescription());
//        tBirdColors.setText(mBird.get);
//        tBirdShapes.setText(mBird.get);
//        tBirdHabitats.setText(mBird.get);
//        tBirdFacts.setText(mBird.get);
    }

    @Override
    public void finish() {
        Log.i(LOG_TAG, "--Exit bird page.--");
        selectedBird = null;
        super.finish();
    }
}