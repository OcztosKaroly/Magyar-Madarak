package com.example.magyar_madarak.ui.Pages;

import static com.example.magyar_madarak.ui.Adapters.ListBirdsAdapter.selectedBird;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.magyar_madarak.HunBirdApplication;
import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Diet;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        if (selectedBird == null) {
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
        Glide.with(this)
                .load(new File(new File(HunBirdApplication.getAppContext().getFilesDir(), "main_bird_images/" + mBird.getBirdName() + ".jpg").getAbsolutePath()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_placeholder_image)
                .transform(new RoundedCorners(15))
                .into(imageBird);

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

    private void setBirdTexts() {
        tBirdName.setText(capitalizeFirstLetter(mBird.getBirdName()));
        tBirdLatinName.setText(mBird.getLatinName());
        tBirdMigratory.setText(mBird.isMigratory() ? "költöző" : "itthon telelő");
        tBirdSize.setText(mBird.getSize());
        tBirdWingSpan.setText(mBird.getWingSpan());
        tBirdConservationValue.setText(mBird.getConservation());

        tBirdDescription.setText(mBird.getDescription());

        List<String> diets = mBird.getDiets().stream().map(Diet::getDietName).collect(Collectors.toList());
        List<String> colors = mBird.getColors().stream().map(Color::getColorName).collect(Collectors.toList());
        List<String> shapes = mBird.getShapes().stream().map(Shape::getShapeName).collect(Collectors.toList());
        List<String> habitats = mBird.getHabitats().stream().map(Habitat::getHabitatName).collect(Collectors.toList());
        List<String> facts = mBird.getFacts();
        createListForLayout((ArrayList<String>) diets, listBirdDiets);
        createListForLayout((ArrayList<String>) colors, listBirdColors);
        createListForLayout((ArrayList<String>) shapes, listBirdShapes);
        createListForLayout((ArrayList<String>) habitats, listBirdHabitats);
        createListForLayout((ArrayList<String>) facts, listBirdFacts);
    }

    private void createListForLayout(ArrayList<String> list, LinearLayout layout) {
        if (list.isEmpty() || list.get(0).isEmpty()) {
            try {
                LinearLayout parent = (LinearLayout) layout.getParent();
                parent.setVisibility(View.INVISIBLE);
            } catch (Error e) {
                Log.e(LOG_TAG, "--List parent is not LinearLayout!--", e);
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