package com.example.magyar_madarak;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;
import static com.example.magyar_madarak.utils.NavigationUtils.redirect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KnowledgeBaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = KnowledgeBaseActivity.class.getName();

    private Button redirectToLoginBTN, redirectToRegisterBTN;

    private View mView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_knowledge_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contentKnowledgebase), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentKnowledgebase);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().getItem(0).setChecked(true);

        redirectToLoginBTN = findViewById(R.id.btnKnowledgebaseToLogin);
        redirectToRegisterBTN = findViewById(R.id.btnKnowledgebaseToRegister);

        initializeListeners();
    }

    private void initializeListeners() {
        redirectToLoginBTN.setOnClickListener(v -> redirect(this, LoginActivity.class));
        redirectToRegisterBTN.setOnClickListener(v -> redirect(this, RegisterActivity.class));

        navigationBarRedirection(mBottomNavigationView, this);
    }
}