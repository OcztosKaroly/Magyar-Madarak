package com.example.magyar_madarak.data.repository;

import static com.example.magyar_madarak.utils.AuthUtils.isUserAuthenticated;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.observation.ObservationDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.observation.Observation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservationRepository {
    private static final String LOG_TAG = "OBSERVATION_DATA";

    private ObservationDAO observationDAO;

    private final ExecutorService executorService;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private CollectionReference mObservationsCollection;

    public ObservationRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        observationDAO = db.observationDAO();

        executorService = Executors.newSingleThreadExecutor();

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mObservationsCollection = mFirestore.collection("observations");

        syncObservationsWithFirestore();
    }

    public void createObservation(String userId, String observationName, Date observationDate, String description) {
        executorService.execute(() -> {
            Date currentDate = new Date();
            Observation observation = new Observation(
                    mFirestore.collection("observations").document().getId(),
                    userId,
                    currentDate,
                    currentDate,
                    observationName,
                    observationDate,
                    description
            );

            observationDAO.insert(observation);
            mObservationsCollection.document(observation.getObservationId()).set(observation);
        });
    }

    public void insertObservation(Observation observation) {
        executorService.execute(() -> {
            observationDAO.insert(observation);
            mObservationsCollection.document(observation.getObservationId()).set(observation);
        });
    }

    public LiveData<Observation> getObservationById(String observationId) {
        return observationDAO.getById(observationId);
    }

    public LiveData<List<Observation>> getAllObservationByUserId(String userId) {
        return observationDAO.getAllByUserId(userId);
    }

    public void updateObservation(Observation observation) {
        executorService.execute(() -> {
            observationDAO.update(observation);
            mObservationsCollection.document(observation.getObservationId()).set(observation);
        });
    }

    public void deleteObservationById(String observationId) {
        executorService.execute(() -> {
            observationDAO.deleteById(observationId);
            mObservationsCollection.document(observationId).delete();
        });
    }

    public void deleteObservation(Observation observation) {
        this.deleteObservationById(observation.getObservationId());
    }

    public void deleteAllObservationsByUserId(String userId) {
        executorService.execute(() -> {
            observationDAO.deleteAllByUserId(userId);
            mObservationsCollection
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot observation: task.getResult()) {
                                mObservationsCollection.document(observation.getId()).delete();
                            }
                        } else {
                            Log.e(LOG_TAG, "FireBase query (delete all observations of user) failed.", task.getException());
                        }
                    });
        });
    }

    private void syncObservationsWithFirestore() {
        if (isUserAuthenticated()) {
            mObservationsCollection.whereEqualTo("userId", mAuth.getUid()).get().addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Observation observation = new Observation(
                                    document.getId(),
                                    Objects.requireNonNull(document.getString("userId")),
                                    Objects.requireNonNull(document.getDate("creationDate")),
                                    Objects.requireNonNull(document.getDate("lastModificationDate")),
                                    Objects.requireNonNull(document.getString("name")),
                                    document.getDate("observationDate"),
                                    document.getString("description")
                            );
                            insertObservation(observation);
                            Log.d(LOG_TAG, "--Firestore observation got: " + observation + ".--");
                        }
                    }).addOnFailureListener(e -> {
                        Log.e(LOG_TAG, "--Firestore observation query error: --", e);
                    });
        }
    }
}