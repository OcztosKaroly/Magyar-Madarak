package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.data.repository.BirdRepository;

import java.util.List;

public class BirdViewModel extends AndroidViewModel {
    private BirdRepository birdRepository;

    public BirdViewModel(Application application) {
        super(application);

        birdRepository = new BirdRepository(application);
    }

    // This function is unnecessary due to the user cannot create, modify or delete any birds.
    // public void insertBird(Bird bird) {
    //     birdRepository.insertBird(bird);
    // }

    public LiveData<Bird> getBirdById(String birdId) {
        return birdRepository.getBirdById(birdId);
    }

    public LiveData<Bird> getBirdByName(String name) {
        return birdRepository.getBirdByName(name);
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birdRepository.getAllBirds();
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
}
