package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.Observation;
import com.example.magyar_madarak.data.repository.ObservationRepository;

import java.util.List;

public class ObservationViewModel extends AndroidViewModel {
    private ObservationRepository observationRepository;

    public ObservationViewModel(Application application) {
        super(application);

        observationRepository = new ObservationRepository(application);
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

    public void updateObservation(Observation observation) {
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
