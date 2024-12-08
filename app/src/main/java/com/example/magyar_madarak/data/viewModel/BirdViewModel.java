package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.example.magyar_madarak.data.repository.BirdRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public LiveData<List<Color>> getAllColorsByNames(List<String> colorNames) {
        if (colorNames == null || colorNames.isEmpty()) {
            return new MediatorLiveData<>();
        }

        return Transformations.map(getAllColors(), colors -> {
            if (colors == null || colors.isEmpty()) {
                return new ArrayList<>();
            }

            return colors.stream()
                    .filter(color -> colorNames.contains(color.getColorName()))
                    .collect(Collectors.toList());
        });
    }

    public LiveData<List<Shape>> getAllShapes() {
        return birdRepository.getAllBirdShapes();
    }

    public LiveData<List<Shape>> getAllShapesByNames(List<String> shapeNames) {
        if (shapeNames == null || shapeNames.isEmpty()) {
            return new MediatorLiveData<>();
        }

        return Transformations.map(getAllShapes(), shapes -> {
            if (shapes == null || shapes.isEmpty()) {
                return new ArrayList<>();
            }

            return shapes.stream()
                    .filter(shape -> shapeNames.contains(shape.getShapeName()))
                    .collect(Collectors.toList());
        });
    }

    public LiveData<List<Habitat>> getAllHabitats() {
        return birdRepository.getAllBirdHabitats();
    }

    public LiveData<List<Habitat>> getAllHabitatsByNames(List<String> habitatNames) {
        if (habitatNames == null || habitatNames.isEmpty()) {
            return new MediatorLiveData<>();
        }

        return Transformations.map(getAllHabitats(), habitats -> {
            if (habitats == null || habitats.isEmpty()) {
                return new ArrayList<>();
            }

            return habitats.stream()
                    .filter(habitat -> habitatNames.contains(habitat.getHabitatName()))
                    .collect(Collectors.toList());
        });
    }
}
