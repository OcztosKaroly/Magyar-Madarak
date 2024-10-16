package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.ui.Adapters.KnowledgeBaseAdapter.selectedBird;
import static com.example.magyar_madarak.utils.CommonUtils.capitalizeFirstLetter;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Bird;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class BirdPageActivity extends AppCompatActivity {
    private static final String LOG_TAG = BirdPageActivity.class.getName();

    private MaterialToolbar toolbar;

    private TextView tBirdName, tBirdLatinName, tBirdMigratory, tBirdSize, tBirdWingSpan, tBirdConservationValue, tBirdDescription;
    private LinearLayout listBirdDiets, listBirdColors, listBirdShapes, listBirdHabitats, listBirdFacts;
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
        tBirdMigratory = findViewById(R.id.tBirdMigratory);
        tBirdSize = findViewById(R.id.tBirdSize);
        tBirdWingSpan = findViewById(R.id.tBirdWingSpan);
        tBirdConservationValue = findViewById(R.id.tBirdConservationValue);
        tBirdDescription = findViewById(R.id.tBirdDescription);

        listBirdDiets = findViewById(R.id.listBirdDiets);
        listBirdColors = findViewById(R.id.listBirdColors);
        listBirdShapes = findViewById(R.id.listBirdShapes);
        listBirdHabitats = findViewById(R.id.listBirdHabitats);
        listBirdFacts = findViewById(R.id.listBirdFacts);

        setBirdTexts();

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

    private void setBirdTexts() {
        tBirdName.setText(capitalizeFirstLetter(mBird.getName()));
        tBirdLatinName.setText(mBird.getLatinName());
        tBirdMigratory.setText(mBird.isMigratory() ? "költöző" : "itthon telelő");
        tBirdSize.setText(mBird.getSize());
        tBirdWingSpan.setText(mBird.getWingSpan());
        tBirdConservationValue.setText(mBird.getConservation());

        tBirdDescription.setText(mBird.getDescription());

        createListForLayout(mBird.getDiets(), listBirdDiets);
        createListForLayout(mBird.getColors(), listBirdColors);
        createListForLayout(mBird.getShapes(), listBirdShapes);
        createListForLayout(mBird.getHabitats(), listBirdHabitats);
        createListForLayout(mBird.getFacts(), listBirdFacts);
    }

    private void createListForLayout(ArrayList<String> list, LinearLayout layout) {
        if (list.isEmpty() || list.get(0).isEmpty()) {
            try {
                LinearLayout parent = (LinearLayout) layout.getParent();
                parent.setVisibility(View.INVISIBLE);
            } catch (Error e) {
                Log.e(LOG_TAG, "--List parent is not LinearLayout! ", e);
            }
            return;
        }

        for (String item : list) {
            TextView textView = new TextView(this);
            item = "- " + item;
            textView.setText(item);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                textView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            }
            layout.addView(textView);
        }
    }

    @Override
    public void finish() {
        Log.i(LOG_TAG, "--Exit bird page.--");
        selectedBird = null;
        super.finish();
    }
}