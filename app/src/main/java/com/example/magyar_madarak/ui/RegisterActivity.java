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

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.utils.AuthUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();

    private EditText emailET, passwordET, rePasswordET;
    private Button registerBTN, registerWithGoogleBTN, redirectToLoginBTN, cancelBTN;

//    private ImageView tooltipEmail;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contentRegister), (v, insets) -> {
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
        mView = findViewById(R.id.contentRegister);

        emailET = findViewById(R.id.etRegisterEmailAddress);
        passwordET = findViewById(R.id.etRegisterPassword);
        rePasswordET = findViewById(R.id.etRegisterRePassword);

        registerBTN = findViewById(R.id.btnRegister);
        registerWithGoogleBTN = findViewById(R.id.btnRegisterWithGoogle);
        redirectToLoginBTN = findViewById(R.id.btnRegisterToLogin);
        cancelBTN = findViewById(R.id.buttonCancel);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        tooltipEmail = findViewById(R.id.tooltipRegisterEmail);

        initializeListeners();
    }

    private void initializeListeners() {
        registerBTN.setOnClickListener(v -> performRegister());
        registerWithGoogleBTN.setOnClickListener(v -> AuthUtils.performAuthWithGoogle(mGoogleSignInClient, this));
        redirectToLoginBTN.setOnClickListener(v -> redirect(this, LoginActivity.class));
        cancelBTN.setOnClickListener(v -> {
            Log.i(LOG_TAG, "--Navigate back.--");
            finish();
        });
    }

    private void performRegister() {
        Log.i(LOG_TAG, "--Initiate registration.--");

        String email = emailET.getText().toString().trim().toLowerCase();
        String password = passwordET.getText().toString();
        String rePassword = rePasswordET.getText().toString();

        if (!isRegistrationDataValid(email, password, rePassword)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "--Registration successful.--");
                        // TODO: Átirányítás a regisztrációról.
                        Toast.makeText(RegisterActivity.this, "Sikeres regisztráció.", Toast.LENGTH_LONG).show();
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(this, emailSenderTask -> {
                            if (emailSenderTask.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Megerősítő email elküldve a " + mAuth.getCurrentUser().getEmail() + " címre.", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Hiba lépett fel a megerősítő email elküldésekor!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Log.e(LOG_TAG, "--Registration error.--");
                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Ez az email cím már foglalt!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Ismeretlen hiba a regisztráció során!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isRegistrationDataValid(String email, String password, String rePassword) {
        // TODO: Foglalt-e már az email-cím az adatbázisban.
        //  Erre és a többire is lehet nincs szükség a FireStore Auth beépített beállításai miatt.
        //if (email cím már foglalt) {
        //    Log.e(LOG_TAG, "This email address already registered.");
        //    Toast.makeText(RegisterActivity.this, "Ez az email cím már foglalt!", Toast.LENGTH_LONG).show();
        //    return false;
        //}

        // nem email lett megadva emailnek
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e(LOG_TAG, "--Incorrect email address.--");
            Toast.makeText(RegisterActivity.this, "Ez az email cím érvénytelen!", Toast.LENGTH_LONG).show();
            return false;
        }

        // nem egyező jelszavak
        if (!password.equals(rePassword)) {
            Log.e(LOG_TAG, "--Passwords not matching.--");
            Toast.makeText(RegisterActivity.this, "Nem egyeznek a jelszavak!", Toast.LENGTH_LONG).show();
            return false;
        }

        // túl rövid a jelszó
        if (password.length() < 12) {
            //TODO: Jelszóbonyolultság ellenőrzést felvenni, szóközök létét átgondolni és kezelni.
            Log.e(LOG_TAG, "--Password too short.--");
            Toast.makeText(this, "A jelszónak legalább 12 karakteresnek kell lennie!", Toast.LENGTH_LONG).show();
            return false;
        }

        // túl hosszú a jelszó
        if (password.length() > 127) {
            Log.e(LOG_TAG, "--Password too long. (max 127)--");
            Toast.makeText(RegisterActivity.this, "Túl hosszú a jelszó! (max 127)", Toast.LENGTH_LONG).show();
            return false;
        }

        // Minden sikeresen ment
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AuthUtils.handleGoogleAuthResult(requestCode, data, mAuth, this);
    }
}
