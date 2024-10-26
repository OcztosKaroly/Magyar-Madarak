package com.example.magyar_madarak.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.ObservationDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.Observation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObservationRepository {
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
        //TODO: syncWithFirestore
    }

    public void insertObservation(Observation observation) {
        executorService.execute(() -> {
            observationDAO.insert(observation);
            mObservationsCollection.document(observation.getObservationId()).set(observation);
        });
    }

    public LiveData<Observation> getObservationById(String observationId) {
        return observationDAO.getObservationById(observationId);
    }

    public LiveData<List<Observation>> getAllObservationByUserId(String userId) {
        return observationDAO.getAllObservationByUserId(userId);
    }

    public void updateObservation(Observation observation) {
        executorService.execute(() -> {
            observationDAO.updateObservation(observation);
            mObservationsCollection.document(observation.getObservationId()).set(observation);
        });
    }

    public void deleteObservationById(String observationId) {
        executorService.execute(() -> {
            observationDAO.deleteObservationById(observationId);
            mObservationsCollection.document(observationId).delete();
        });
    }

    public void deleteObservation(Observation observation) {
        this.deleteObservationById(observation.getObservationId());
    }
}