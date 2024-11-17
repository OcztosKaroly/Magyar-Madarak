package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.observation.Observation;
import com.example.magyar_madarak.data.repository.ObservationRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ObservationViewModel extends AndroidViewModel {
    private ObservationRepository observationRepository;
    private FirebaseAuth mAuth;

    public ObservationViewModel(Application application) {
        super(application);

        observationRepository = new ObservationRepository(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void createObservation(String observationName, Date observationDate, String description) {
        observationRepository.createObservation(
                Objects.requireNonNull(mAuth.getUid()),
                observationName,
                observationDate,
                description
        );
    }

    public void insertObservation(Observation observation) {
        observationRepository.insertObservation(observation);
    }

    public LiveData<Observation> getObservationById(String observationId) {
        return observationRepository.getObservationById(observationId);
    }

    public LiveData<List<Observation>> getAllObservationsByUserId(String userId) {
        return observationRepository.getAllObservationByUserId(userId);
    }

    public LiveData<List<Observation>> getAllObservations() {
        return observationRepository.getAllObservationByUserId(mAuth.getUid());
    }

    public void updateObservation(Observation observation) {
        observation.setLastModificationDate(new Date());
        observationRepository.updateObservation(observation);
    }

    public void deleteObservationById(String observationId) {
        observationRepository.deleteObservationById(observationId);
    }

    public void deleteObservation(Observation observation) {
        observationRepository.deleteObservation(observation);
    }

    public void deleteAllObservationsByUserId(String userId) {
        observationRepository.deleteAllObservationsByUserId(userId);
    }
}
