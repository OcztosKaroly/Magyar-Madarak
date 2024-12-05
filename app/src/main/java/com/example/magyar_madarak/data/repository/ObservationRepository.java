package com.example.magyar_madarak.data.repository;

import static com.example.magyar_madarak.utils.AuthUtils.getCurrentUser;
import static com.example.magyar_madarak.utils.AuthUtils.isUserAuthenticated;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.observation.ObservationDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.observation.Observation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservationRepository {
    private static final String LOG_TAG = "OBSERVATION_DATA";

    private ObservationDAO observationDAO;

    private final ExecutorService executorService;

    private FirebaseFirestore mFirestore;
    private CollectionReference mObservationsCollection;

    public ObservationRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        observationDAO = db.observationDAO();

        executorService = Executors.newSingleThreadExecutor();

        mFirestore = FirebaseFirestore.getInstance();
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
            setFirestoreObservation(observation);
        });
    }

    public LiveData<Observation> getObservationById(String observationId) {
        return observationDAO.getById(observationId);
    }

    public LiveData<List<Observation>> getAllObservation() {
        return observationDAO.getAll();
    }

    public void updateObservation(Observation observation) {
        executorService.execute(() -> {
            observationDAO.update(observation);
            setFirestoreObservation(observation);
        });
    }

    // create, update
    private void setFirestoreObservation(Observation observation) {
        if (!isUserAuthenticated()) {
            return;
        }
        // create Firestore object; bc the observationId stored in Observation object too, but we store in Firestore as document id
        Map<String, Object> firestoreObservation = new HashMap<>();
        firestoreObservation.put("userId", observation.getUserId());
        firestoreObservation.put("creationDate", observation.getCreationDate());
        firestoreObservation.put("lastModificationDate", observation.getLastModificationDate());
        firestoreObservation.put("name", observation.getName());
        firestoreObservation.put("observationDate", observation.getObservationDate());
        firestoreObservation.put("description", observation.getDescription());

        mObservationsCollection.document(observation.getObservationId())
                .set(firestoreObservation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "--Observation (" + observation.getName() + ") successfully created.--");
                    } else {
                        Log.e(LOG_TAG, "--Error occurred when creating observation (" + observation.getName() + ").--", task.getException());
                    }
        });
    }

    public void deleteObservationById(String observationId) {
        executorService.execute(() -> {
            observationDAO.deleteById(observationId);
            if (isUserAuthenticated()) {
                mObservationsCollection.document(observationId).delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "--FireBase observation (" + observationId + ") successfully deleted.--");
                    } else {
                        Log.e(LOG_TAG, "--FireBase query (delete observation by id) failed.--", task.getException());
                    }
                });
            }
        });
    }

    public void deleteObservation(Observation observation) {
        this.deleteObservationById(observation.getObservationId());
    }

    public void deleteAllObservationsByUserId(String userId) {
        executorService.execute(() -> {
            observationDAO.deleteAll();
            deleteAllObservationsFromFirestoreByUserId(userId);
        });
    }

    public void deleteAllObservationsFromFirestoreByUserId(String userId) {
        if (!isUserAuthenticated()) {
            return;
        }
        mObservationsCollection.whereEqualTo("userId", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot observation: task.getResult()) {
                    mObservationsCollection.document(observation.getId()).delete();
                }
            } else {
                Log.e(LOG_TAG, "--FireBase query (delete all observations of user) failed.--", task.getException());
            }
        });
    }

    private void syncObservationsWithFirestore() {
        if (!isUserAuthenticated()) {
            return;
        }

        String currentUserId = getCurrentUser().getUid();

        mObservationsCollection.whereEqualTo("userId", currentUserId).get().addOnSuccessListener(querySnapshot -> {
            List<Observation> firestoreObservations = new ArrayList<>();
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
                firestoreObservations.add(observation);
            }

            executorService.execute(() -> {
                List<Observation> localObservations = observationDAO.getAllForSync();
                List<Observation> observationsToRemove = new ArrayList<>();

                for (Observation observation : localObservations) {
                    if (!observation.getUserId().equals("local") && !observation.getUserId().equals(currentUserId)) {
                        observationsToRemove.add(observation);
                    }
                }

                for (Observation observation : observationsToRemove) {
                    localObservations.remove(observation);
                    observationDAO.deleteById(observation.getObservationId());
                }

                for (Observation firestoreObservation: firestoreObservations) {
                    Observation localObservation = observationDAO.findById(firestoreObservation.getObservationId());

                    if (localObservation == null || firestoreObservation.getLastModificationDate().after(localObservation.getLastModificationDate())) {
                        observationDAO.insert(firestoreObservation);
                        Log.d(LOG_TAG, "--Firestore observation got: " + firestoreObservation + ".--");
                    }
                }

                for (Observation localObservation: localObservations) {
                    Observation firestoreObservation = firestoreObservations.stream().filter(observation ->
                            observation.getObservationId().equals(localObservation.getObservationId())
                    ).findFirst().orElse(null);

                    if (firestoreObservation == null || localObservation.getLastModificationDate().after(firestoreObservation.getLastModificationDate())) {
                        localObservation.setUserId(currentUserId);
                        updateObservation(localObservation);
                        Log.d(LOG_TAG, "--Firestore observation set: " + localObservation + ".--");
                    }
                }
            });
        }).addOnFailureListener(e -> {
            Log.e(LOG_TAG, "--Firestore observation query error: --", e);
        });
    }
}
