package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.AuthUtils.isUserAuthenticated;
import static com.example.magyar_madarak.utils.NavigationUtils.redirect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.viewModel.ObservationViewModel;
import com.example.magyar_madarak.utils.AuthUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();

    EditText emailET, passwordET;
    Button loginBTN, loginWithGoogleBTN, redirectToRegisterBTN, cancelBTN;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contentLogin), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (isUserAuthenticated()) {
            finish();
            return;
        }

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentLogin);

        emailET = findViewById(R.id.etLoginEmailAddress);
        passwordET = findViewById(R.id.etLoginPassword);

        loginBTN = findViewById(R.id.btnLogin);
        loginWithGoogleBTN = findViewById(R.id.btnLoginWithGoogle);
        redirectToRegisterBTN = findViewById(R.id.btnLoginToRegister);
        cancelBTN = findViewById(R.id.buttonCancel);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initializeListeners();
    }

    private void initializeListeners() {
        loginBTN.setOnClickListener(v -> performLogin());
        loginWithGoogleBTN.setOnClickListener(v -> AuthUtils.performAuthWithGoogle(mGoogleSignInClient, this));
        redirectToRegisterBTN.setOnClickListener(v -> redirect(this, RegisterActivity.class));
        cancelBTN.setOnClickListener(v -> {
            Log.i(LOG_TAG, "--Navigate back.--");
            finish();
        });

    }

    private void performLogin() {
        Log.i(LOG_TAG, "--Initiate login.--");

        String email = emailET.getText().toString().trim().toLowerCase();
        String password = passwordET.getText().toString();

        if (!isLoginDataValid(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.i(LOG_TAG, "--Login successful.--");
                Toast.makeText(LoginActivity.this, "Sikeres bejelentkezés.", Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, "--Bejelentkezési adatok: " + FirebaseAuth.getInstance().getCurrentUser());
                syncObservations();
                finish();
            } else {
                Log.e(LOG_TAG, "--Login error.--");
                Toast.makeText(LoginActivity.this, "Hibás felhasználónév vagy jelszó!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isLoginDataValid(String email, String password) {
        if (email.isEmpty()) {
            Log.e(LOG_TAG, "--Email address is null.--");
            Toast.makeText(LoginActivity.this, "Email cím kitöltése kötelező!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.isEmpty()) {
            Log.e(LOG_TAG, "--Password is null.--");
            Toast.makeText(LoginActivity.this, "Jelszó kitöltése kötelező!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e(LOG_TAG, "--Email address invalid.--");
            Toast.makeText(LoginActivity.this, "Hibás felhasználónév vagy jelszó!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void syncObservations() {
        ObservationViewModel observationViewModel = new ViewModelProvider(this).get(ObservationViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AuthUtils.handleGoogleAuthResult(requestCode, data, mAuth, this);
    }
}
