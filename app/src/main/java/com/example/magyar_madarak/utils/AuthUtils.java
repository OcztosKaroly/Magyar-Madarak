package com.example.magyar_madarak.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.magyar_madarak.HunBirdApplication;
import com.example.magyar_madarak.data.viewModel.ObservationViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class AuthUtils {
    private static final String LOG_TAG = "AUTHENTICATION";
    private static final int REQUEST_CODE = 74;

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void performAuthWithGoogle(GoogleSignInClient mGoogleSignInClient, Activity activity) {
        Log.i(LOG_TAG, "--Initiate login with Google.--");
        activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE);
    }

    public static void handleGoogleAuthResult(int requestCode, Intent data, FirebaseAuth auth, Activity activity) {
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
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
                Toast.makeText(activity, "Sikeres bejelentkezés Google fiókkal.", Toast.LENGTH_SHORT).show();
                ObservationViewModel observationViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ObservationViewModel.class);
                activity.finish();
            } else {
                Log.e(LOG_TAG, "--Google login/registration to app unsuccessful.--");
                Toast.makeText(activity, "Hiba a Google bejelentkezés során!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void logout(Activity activity) {
        if (isUserAuthenticated()) {
            Log.i(LOG_TAG, "--Signing out.--");
            Toast.makeText(HunBirdApplication.getAppContext(), "Kijelentkezés...", Toast.LENGTH_SHORT).show();
//            clearAllUserData();
            mAuth.signOut();

            logoutFromGoogle(activity);
        }
    }

    public static void logoutFromGoogle(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);

        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Log.i(LOG_TAG, "--Google Sign-In logout successful.--");
            Toast.makeText(activity, "Sikeres kijelentkezés.", Toast.LENGTH_SHORT).show();
        });
    }

//    // possibly used for log out data deletion
//    public static void clearAllUserData() {
//        HunBirdsRoomDatabase database = HunBirdsRoomDatabase.getInstance(getAppContext());
//
//        UserDAO userDao = database.userDAO();
//        ObservationDAO observationsDao = database.observationDAO();
//
//        String userId = getCurrentUser().getUid();
//
//        Executors.newSingleThreadExecutor().execute(() -> {
//            userDao.deleteById(userId); // Nem tárolunk el adatokat a felhasználókról külön objektumban
//            observationsDao.deleteAll();
//        });
//    }

    public static boolean isUserAuthenticated() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (currentUser.isEmailVerified()) {
                        Log.i(LOG_TAG, "--User authenticated and email verified.--");
                    } else {
                        Log.w(LOG_TAG, "--User email still not verified.--");
                    }
                } else {
                    Log.e(LOG_TAG, "--Failed to reload user data.--");
                }
            });
            return currentUser.isEmailVerified();
        }

        Log.w(LOG_TAG, "--No authenticated user detected.--");
        return false;
    }


    public static FirebaseUser getCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }
}
