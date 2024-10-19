package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.data.repository.BirdRepository;

import java.util.List;

public class BirdViewModel extends AndroidViewModel {
    private BirdRepository birdRepository;

    private LiveData<List<Bird>> birds;

    public BirdViewModel(Application application) {
        super(application);

        birdRepository = new BirdRepository(application);
        birds = birdRepository.getAllBirds();
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birds;
    }

    public LiveData<List<Bird>> getBirdsByNameList(List<String> birdsNames) {
        return birdRepository.getBirdsByNames(birdsNames);
    }

    public LiveData<List<String>> getAllColors() {
        return birdRepository.getAllUniqueBirdColors();
    }

    public LiveData<List<String>> getAllShapes() {
        return birdRepository.getAllUniqueBirdShapes();
    }

    public LiveData<List<String>> getAllHabitats() {
        return birdRepository.getAllUniqueBirdHabitats();
    }

    public void insertBird(Bird bird) {
        this.birdRepository.insertBird(bird);
    }
}
