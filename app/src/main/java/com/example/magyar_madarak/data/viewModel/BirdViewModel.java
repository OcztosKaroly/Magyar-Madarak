package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.example.magyar_madarak.data.repository.BirdRepository;

import java.util.List;

public class BirdViewModel extends AndroidViewModel {
    private BirdRepository birdRepository;

    public BirdViewModel(Application application) {
        super(application);

        birdRepository = new BirdRepository(application);
    }

    public LiveData<Bird> getBirdById(String birdId) {
        return birdRepository.getBirdById(birdId);
    }

    public LiveData<List<Bird>> getBirdByName(String name) {
        return birdRepository.getBirdsByName(name);
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birdRepository.getAllBirds();
    }

    public LiveData<List<Bird>> getBirdsByNameList(List<String> birdsNames) {
        return birdRepository.getBirdsByNames(birdsNames);
    }

    public LiveData<List<Color>> getAllColors() {
        return birdRepository.getAllBirdColors();
    }

    public LiveData<List<Shape>> getAllShapes() {
        return birdRepository.getAllBirdShapes();
    }

    public LiveData<List<Habitat>> getAllHabitats() {
        return birdRepository.getAllBirdHabitats();
    }
}
