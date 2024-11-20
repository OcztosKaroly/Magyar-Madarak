package com.example.magyar_madarak.utils;

import static com.example.magyar_madarak.HunBirdApplication.getAppContext;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.magyar_madarak.data.dao.observation.ObservationDAO;
import com.example.magyar_madarak.data.dao.user.UserDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.repository.ObservationRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

public class AuthUtils {
    private static final String LOG_TAG = "AUTHENTICATION";
    private static final int REQUEST_CODE = 74;

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void performAuthWithGoogle(GoogleSignInClient mGoogleSignInClient, Activity activity) {
        Log.i(LOG_TAG, "--Initiate login/registration with Google.--");
        mGoogleSignInClient.signOut().addOnCompleteListener(activity, task -> {
            Intent googleRegisterIntent = mGoogleSignInClient.getSignInIntent();
            activity.startActivityForResult(googleRegisterIntent, REQUEST_CODE);
        });
        //TODO: A felső megoldás a Google regisztrációs funkció teszteléséig bent marad, a végleges alkalmazásban az alsó megoldás fog helyet kapni
        //Intent googleRegisterIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(googleRegisterIntent, RC_REGISTER);
    }

    public static void handleGoogleAuthResult(int requestCode, Intent data, FirebaseAuth auth, Activity activity) {
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(LOG_TAG, "--Google account email: " + account.getEmail());
                //Log.d(LOG_TAG, "google account information: " + account.getPhotoUrl());
                firebaseAuthWithGoogle(account.getIdToken(), auth, activity);
            } catch (ApiException e) {
                Log.e(LOG_TAG, "--Google sign in error: " + e + ".--");
            }
        }
    }

    private static void firebaseAuthWithGoogle(String idToken, FirebaseAuth auth, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                Log.i(LOG_TAG, "--Google login/registration to app successful.--");
                // TODO: Átirányítás a regisztrációról regisztráció után.
                Toast.makeText(activity, "Sikeres bejelentkezés/regisztráció Google fiókkal.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(LOG_TAG, "--Google login/registration to app unsuccessful.--");
                // TODO: Regisztrációs errorok felsorolása. Toast törlése
                Toast.makeText(activity, "Hiba a Google bejelentkezés/regisztráció során!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void logout() {
        if (isUserAuthenticated()) {
            Log.i(LOG_TAG, "--Signing out.--");
            clearAllUserData();
            mAuth.signOut();
        }
    }

    // possibly used for log out data deletion
    public static void clearAllUserData() {
        HunBirdsRoomDatabase database = HunBirdsRoomDatabase.getInstance(getAppContext());

        UserDAO userDao = database.userDAO();
        ObservationDAO observationsDao = database.observationDAO();

        String userId = getCurrentUser().getUid();

        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.deleteById(userId);
            observationsDao.deleteAllByUserId(userId);
        });
    }

    public static boolean isUserAuthenticated() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (!currentUser.isEmailVerified()) {
                Log.w(LOG_TAG, "--User email still not verified.--");
                return false;
            }
            Log.i(LOG_TAG, "--User authenticated.--");
            return true;
        }
        Log.w(LOG_TAG, "--No authenticated user detected.--");
        return false;
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }
}
