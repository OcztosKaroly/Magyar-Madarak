package com.example.magyar_madarak.data.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.user.UserDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private static final String LOGTAG = "USERDATA";

    private UserDAO userDAO;

    private final ExecutorService executorService;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUsersSettingsCollection;

    private LiveData<User> user;

    public UserRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        userDAO = db.userDAO();

        executorService = Executors.newSingleThreadExecutor();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUsersSettingsCollection = mFirestore.collection("users_settings");
//        syncUserWithFirestore(application);
    }

    public void insertUser(User user) {
        executorService.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserById(String userId) {
        return userDAO.getById(userId);
    }

    public void updateUser(User user) {
        executorService.execute(() -> {
            userDAO.update(user);
            mUsersSettingsCollection.document(user.getUserId()).set(user);
        });
    }

    public void deleteUserById(String userId) {
        executorService.execute(() -> {
            userDAO.deleteById(userId);
            mUsersSettingsCollection.document(userId).delete();
        });
    }

    public void deleteUser(User user) {
        this.deleteUserById(user.getUserId());
    }

    private void syncUserWithFirestore(Application application) {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.w(LOGTAG ,"No registered user detected.");
            return;
        }

        if (currentUser.isEmailVerified()) {
            User user = new User(
                    Objects.requireNonNull(mAuth.getUid()),
                    currentUser.getEmail()
            );
            insertUser(user);
            mUsersSettingsCollection.document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        insertUser(user);
                    });
        } else {
            Toast.makeText(application.getApplicationContext(), "Erősítsd meg az email címet a kiküldött email-ben (" + currentUser.getEmail() + ")!", Toast.LENGTH_LONG).show();
            Log.w(LOGTAG ,"User email not verified yet.");
        }
    }

//    private void syncUserWithFirestore(Application application) {
//        if (isWifiConnected(application)) {
//            mUsers.get()
//                    .addOnSuccessListener(querySnapshot -> {
//                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
//                            User user = document.toObject(User.class);
//
//                            if (user != null) {
//                                user.setUserId(document.getId());
//                                insertUser(user);
//                            } else {
//                                Log.e("DATA", "--Firestore user convertion error.--");
//                            }
//                        }
//                    }).addOnFailureListener(e -> {
//                        Log.e("DATA", "--Firestore user query error: --", e);
//                    });
//        }
//    }
}
