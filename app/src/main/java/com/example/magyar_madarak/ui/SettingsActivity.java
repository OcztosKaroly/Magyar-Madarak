package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.AuthUtils.getCurrentUser;
import static com.example.magyar_madarak.utils.AuthUtils.isUserAuthenticated;
import static com.example.magyar_madarak.utils.AuthUtils.logout;
import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.utils.NavigationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SETTINGS";

    private FirebaseUser mUser;
    private boolean isPasswordChangable;
    private boolean wasAuthenticated;

    private ConstraintLayout profileSettingsLayout, authenticationLayout;

    private Button saveBtn, logoutBtn, deleteProfileBtn, loginBtn, registerBtn;
    private CheckBox dailyNotificationCheckBox;
    private TextView emailAddressTW;
    private EditText newPasswordET, reNewPasswordET, oldPasswordET;

    private SharedPreferences mSharedPreferences;

    private View mView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentSettings);

        mSharedPreferences = getApplicationContext().getSharedPreferences("notifications", Context.MODE_PRIVATE);

        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);

        dailyNotificationCheckBox = findViewById(R.id.checkBoxDailyNotification);
        dailyNotificationCheckBox.setChecked(mSharedPreferences.getBoolean("dailyNotificationEnabled", true));

        profileSettingsLayout = findViewById(R.id.layoutProfileSettings);
        authenticationLayout = findViewById(R.id.layoutAuthentication);

        loginBtn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.btnRegister);

        logoutBtn = findViewById(R.id.buttonLogOut);

        emailAddressTW = findViewById(R.id.twEmailAddress);
        newPasswordET = findViewById(R.id.etNewPassword);
        reNewPasswordET = findViewById(R.id.etReNewPassword);
        oldPasswordET = findViewById(R.id.etOldPassword);

        saveBtn = findViewById(R.id.btnSave);
        deleteProfileBtn = findViewById(R.id.btnDeleteProfile);

        if (!isUserAuthenticated()) {
            profileSettingsLayout.setVisibility(View.GONE);
            authenticationLayout.setVisibility(View.VISIBLE);

            wasAuthenticated = false;
        } else {
            profileSettingsLayout.setVisibility(View.VISIBLE);
            authenticationLayout.setVisibility(View.GONE);

            wasAuthenticated = true;

            mUser = getCurrentUser();
            emailAddressTW.setText(mUser.getEmail());

            if (mUser.getProviderData().stream().anyMatch(userInfo -> EmailAuthProvider.PROVIDER_ID.equals(userInfo.getProviderId()))) {
                isPasswordChangable = true;
            } else {
                isPasswordChangable = false;

                newPasswordET.setVisibility(View.GONE);
                reNewPasswordET.setVisibility(View.GONE);
                oldPasswordET.setVisibility(View.GONE);

                saveBtn.setVisibility(View.GONE);
            }

        }

        initializeListeners();
    }

    private void initializeListeners() {
        dailyNotificationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> applyNotificationsChanges(isChecked));

        if (isUserAuthenticated()) {
            saveBtn.setOnClickListener(l -> {
                if (isPasswordChangable) {
                    save();
                }
            });
            logoutBtn.setOnClickListener(l -> {
                logout();
                reload();
            });
            deleteProfileBtn.setOnClickListener(l -> deleteProfile());
        } else {
            loginBtn.setOnClickListener(l -> NavigationUtils.startActivity(this, LoginActivity.class));
            registerBtn.setOnClickListener(l -> NavigationUtils.startActivity(this, RegisterActivity.class));
        }

        navigationBarRedirection(mBottomNavigationView, this);
    }

    private void applyNotificationsChanges(boolean isChecked) {
        mSharedPreferences.edit().putBoolean("dailyNotificationEnabled", isChecked).apply();
        Toast.makeText(this, "Értesítések változtatásai elmentődtek.", Toast.LENGTH_SHORT).show();
    }

    private void save() {
        String newPassword = newPasswordET.getText().toString();
        String reNewPassword = reNewPasswordET.getText().toString();
        String oldPassword = oldPasswordET.getText().toString();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Nincs mit menteni.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 12) {
            Toast.makeText(this, "A jelszónak legalább 12 karakteresnek kell lennie!", Toast.LENGTH_LONG).show();
            return;
        }

        if (newPassword.length() > 127) {
            Toast.makeText(this, "A jelszó legfeljebb 127 karakteres lehet!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!newPassword.equals(reNewPassword)) {
            Toast.makeText(this, "A jelszó és a megerősítő jelszó nem egyezik!", Toast.LENGTH_LONG).show();
            return;
        }

        if (oldPassword.isEmpty()) {
            Toast.makeText(this, "A régi jelszó megadása kötelező!", Toast.LENGTH_LONG).show();
            return;
        }

        mUser.reauthenticate(EmailAuthProvider.getCredential(mUser.getEmail(), oldPassword)).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUser.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(this, "Változtatások sikeresen elmentődtek.", Toast.LENGTH_SHORT).show();
                        newPasswordET.setText("");
                        reNewPasswordET.setText("");
                        oldPasswordET.setText("");
                    } else {
                        Toast.makeText(this, "Valami hiba történt!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "A régi jelszó téves!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteProfile() {
        if (mUser.getProviderData().stream().anyMatch(userInfo -> EmailAuthProvider.PROVIDER_ID.equals(userInfo.getProviderId()))) {
            String oldPassword = oldPasswordET.getText().toString();

            if (oldPassword.isEmpty()) {
                Toast.makeText(this, "Jelszó megadása kötelező.", Toast.LENGTH_LONG).show();
            } else {
                mUser.reauthenticate(EmailAuthProvider.getCredential(mUser.getEmail(), oldPassword)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        delete();
                    } else {
                        Toast.makeText(this, "A régi jelszó téves!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            delete();
        }
    }

    private void delete() {
        mUser.delete().addOnCompleteListener(deleteTask -> {
            if (deleteTask.isSuccessful()) {
                Toast.makeText(this, "Felhasználó sikeresen törölve.", Toast.LENGTH_SHORT).show();
                reload();
            } else {
                Toast.makeText(this, "Valami hiba történt!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        reload();
    }

    private void reload() {
        try {
            if (wasAuthenticated != isUserAuthenticated()) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "--Error at reloading settings.--", e);
        }
    }
}