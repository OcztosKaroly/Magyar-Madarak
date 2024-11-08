package com.example.magyar_madarak.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.bird.BirdDAO;
import com.example.magyar_madarak.data.dao.constants.ColorDAO;
import com.example.magyar_madarak.data.dao.constants.HabitatDAO;
import com.example.magyar_madarak.data.dao.constants.ShapeDAO;
import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BirdRepository {
    private BirdDAO birdDAO;
    private ColorDAO colorDAO;
    private ShapeDAO shapeDAO;
    private HabitatDAO habitatDAO;

    private final ExecutorService executorService;

    private FirebaseFirestore mFirestore;
    private CollectionReference mBirdsCollection;

    public BirdRepository(Application application) {
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(application);
        birdDAO = db.birdDAO();
        colorDAO = db.colorDAO();
        shapeDAO = db.shapeDAO();
        habitatDAO = db.habitatDAO();


        executorService = Executors.newSingleThreadExecutor();

        mFirestore = FirebaseFirestore.getInstance();
        mBirdsCollection = mFirestore.collection("birds");
//        syncBirdsWithFirestore(application);
    }

    public void insertBird(Bird bird) {
        executorService.execute(() -> birdDAO.insert(bird));
    }

    public LiveData<Bird> getBirdById(String birdId) {
        return birdDAO.getById(birdId);
    }

    public LiveData<List<Bird>> getBirdsByName(String birdName) {
        return birdDAO.getAllByName(birdName);
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birdDAO.getAll();
    }

    public LiveData<List<Bird>> getBirdsByNames(List<String> birdsNames) {
        return birdDAO.getAllByNames(birdsNames);
    }

    public LiveData<List<Color>> getAllBirdColors() {
        return colorDAO.getAll();
    }

    public LiveData<List<Shape>> getAllBirdShapes() {
        return shapeDAO.getAll();
    }

    public LiveData<List<Habitat>> getAllBirdHabitats() {
        return habitatDAO.getAll();
    }

//    private void syncBirdsWithFirestore(Application application) {
//        if (isWifiConnected(application)) {
//            mBirdsCollection.get()
//                    .addOnSuccessListener(querySnapshot -> {
//                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
//                            Bird bird = document.toObject(Bird.class);
//
//                            if (bird != null) {
//                                bird.setBirdId(document.getId());
//                                insertBird(bird);
//                            } else {
//                                Log.e("DATA", "--Firestore birds conversion error.--");
//                            }
//                        }
//                    }).addOnFailureListener(e -> {
//                        Log.e("DATA", "--Firestore birds query error: --", e);
//                    });
//        }
//        //TODO: Legeslegelső alkalommal, amikor még nem szinkronizáltak az adatok,
//        // és nincs wifi-re csatlakozva az ember, nem lesz semmilyen madár sem a lokális adatbázisban,
//        // amivel kezdeni kellene valamit
//    }
}
