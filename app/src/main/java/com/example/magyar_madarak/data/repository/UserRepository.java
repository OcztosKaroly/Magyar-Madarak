package com.example.magyar_madarak.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.UserDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDAO userDAO;

    private final ExecutorService executorService;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUsersCollection;

    private LiveData<User> user;

    public UserRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        userDAO = db.userDAO();

        executorService = Executors.newSingleThreadExecutor();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUsersCollection = mFirestore.collection("users");
//        syncUserWithFirestore(application); // TODO: syncUserWithFirestore
    }

    public void insertUser(User user) {
        executorService.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    public void updateUser(User user) {
        executorService.execute(() -> {
            userDAO.updateUser(user);
            mUsersCollection.document(user.getUserId()).set(user);
        });
    }

    public void deleteUserById(String userId) {
        executorService.execute(() -> {
            userDAO.deleteUserById(userId);
            mUsersCollection.document(userId).delete();
        });
    }

    public void deleteUser(User user) {
        this.deleteUserById(user.getUserId());
    }

    private void syncUserWithFirestore(String userId) {
        mUsersCollection.document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        insertUser(user);
                    } else {
                        Log.e("DATA", "--Firestore user not found!--");
                    }
                });
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
