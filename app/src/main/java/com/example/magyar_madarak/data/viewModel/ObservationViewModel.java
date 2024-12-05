package com.example.magyar_madarak.data.viewModel;

import static com.example.magyar_madarak.utils.AuthUtils.getCurrentUser;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.observation.Observation;
import com.example.magyar_madarak.data.repository.ObservationRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ObservationViewModel extends AndroidViewModel {
    private ObservationRepository observationRepository;

    public ObservationViewModel(Application application) {
        super(application);

        observationRepository = new ObservationRepository(application);
    }

    public void createObservation(String observationName, Date observationDate, String description) {
        observationRepository.createObservation(
                getCurrentUser() != null ? getCurrentUser().getUid() : "local",
                observationName,
                observationDate,
                description
        );
    }

    public void insertObservation(Observation observation) {
        observationRepository.createObservation(
                observation.getUserId(),
                observation.getName(),
                observation.getObservationDate(),
                observation.getDescription()
        );
    }

    public LiveData<Observation> getObservationById(String observationId) {
        return observationRepository.getObservationById(observationId);
    }

    public LiveData<List<Observation>> getAllObservations() {
        return observationRepository.getAllObservation();
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

    public void deleteAllObservationsFromFirestoreByUserId(String userId) {
        observationRepository.deleteAllObservationsFromFirestoreByUserId(userId);
    }
}
