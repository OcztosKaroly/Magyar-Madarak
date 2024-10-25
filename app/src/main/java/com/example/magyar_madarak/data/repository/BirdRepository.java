package com.example.magyar_madarak.data.repository;

import static com.example.magyar_madarak.utils.ResourceAvailabilityUtils.isWifiConnected;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.magyar_madarak.data.dao.BirdDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.utils.ConverterUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BirdRepository {
    private BirdDAO birdDAO;

    private final ExecutorService executorService;

    private FirebaseFirestore mFirestore;
    private CollectionReference mBirdsCollection;

    public BirdRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        birdDAO = db.birdDao();

        executorService = Executors.newSingleThreadExecutor();

        mFirestore = FirebaseFirestore.getInstance();
        mBirdsCollection = mFirestore.collection("birds");
        syncBirdsWithFirestore(application);
    }

    // This function will insert only to RoomDB
    public void insertBird(Bird bird) {
        executorService.execute(() -> {
            birdDAO.insert(bird);
        });
    }

    public LiveData<Bird> getBirdById(String birdId) {
        return birdDAO.getBirdById(birdId);
    }

    public LiveData<Bird> getBirdByName(String birdName) {
        return birdDAO.getBirdByName(birdName);
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birdDAO.getAllBirds();
    }

    public LiveData<List<Bird>> getBirdsByNames(List<String> birdsNames) {
        return birdDAO.getBirdsByNames(birdsNames);
    }

    public LiveData<List<String>> getAllUniqueBirdColors() {
        MediatorLiveData<List<String>> uniqueColorsLiveData = new MediatorLiveData<>();

        uniqueColorsLiveData.addSource(birdDAO.getAllColors(), allColorList -> {
            executorService.execute(() -> {
                Set<String> uniqueColors = new HashSet<>();
                for (String birdColors : allColorList) {
                    ArrayList<String> colors = ConverterUtils.fromStringToArrayList(birdColors);
                    uniqueColors.addAll(colors);
                }
                uniqueColorsLiveData.postValue(new ArrayList<>(uniqueColors));
            });
        });

        return uniqueColorsLiveData;
    }

    public LiveData<List<String>> getAllUniqueBirdShapes() {
        MediatorLiveData<List<String>> uniqueShapesLiveData = new MediatorLiveData<>();

        uniqueShapesLiveData.addSource(birdDAO.getAllShapes(), allShapeList -> {
            executorService.execute(() -> {
                Set<String> uniqueShapes = new HashSet<>();
                for (String birdShapes : allShapeList) {
                    ArrayList<String> shapes = ConverterUtils.fromStringToArrayList(birdShapes);
                    uniqueShapes.addAll(shapes);
                }
                uniqueShapesLiveData.postValue(new ArrayList<>(uniqueShapes));
            });
        });

        return uniqueShapesLiveData;
    }

    public LiveData<List<String>> getAllUniqueBirdHabitats() {
        MediatorLiveData<List<String>> uniqueHabitatsLiveData = new MediatorLiveData<>();

        uniqueHabitatsLiveData.addSource(birdDAO.getAllHabitats(), allHabitatList -> {
            executorService.execute(() -> {
                Set<String> uniqueHabitats = new HashSet<>();
                for (String birdHabitats : allHabitatList) {
                    ArrayList<String> habitats = ConverterUtils.fromStringToArrayList(birdHabitats);
                    uniqueHabitats.addAll(habitats);
                }
                uniqueHabitatsLiveData.postValue(new ArrayList<>(uniqueHabitats));
            });
        });

        return uniqueHabitatsLiveData;
    }

    private void syncBirdsWithFirestore(Application application) {
        if (isWifiConnected(application)) {
            mBirdsCollection.get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Bird bird = document.toObject(Bird.class);

                            if (bird != null) {
                                bird.setBirdId(document.getId());
                                insertBird(bird);
                            } else {
                                Log.e("DATA", "--Firestore birds conversion error.--");
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("DATA", "--Firestore birds query error: --", e);
                    });
        }
        //TODO: Legeslegelső alkalommal, amikor még nem szinkronizáltak az adatok,
        // és nincs wifi-re csatlakozva az ember, nem lesz semmilyen madár sem a lokális adatbázisban,
        // amivel kezdeni kellene valamit
    }
}
