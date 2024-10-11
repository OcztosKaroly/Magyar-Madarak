package com.example.magyar_madarak.data.repository;

import static com.example.magyar_madarak.utils.ResourceAvailabilityUtils.isWifiConnected;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.BirdDAO;
import com.example.magyar_madarak.data.database.BirdRoomDatabase;
import com.example.magyar_madarak.data.model.Bird;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BirdRepository {
    private BirdDAO birdDAO;

    private LiveData<List<Bird>> birds;

    private FirebaseFirestore mFirestore;
    private CollectionReference mBirds;

    public BirdRepository(Application application) {
        BirdRoomDatabase db = BirdRoomDatabase.getInstance(application);
        birdDAO = db.birdDao();
        birds = birdDAO.getAllBirds();

        mFirestore = FirebaseFirestore.getInstance();
        mBirds = mFirestore.collection("birds");
        getBirdsFromFirestore(application);
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birds;
    }

    public LiveData<Bird> getBirdByName(String birdName) {
        return birdDAO.getBirdByName(birdName);
    }

    public void insertBird(Bird bird) {
        new Insert(birdDAO).execute(bird);
    }

    private static class Insert extends AsyncTask<Bird, Void, Void> {
        private BirdDAO mDao;

        Insert(BirdDAO dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Bird... birds) {
            mDao.insert(birds[0]);
            return null;
        }
    }

    // TODO: Firestore-ból lekérni a madarakat és beszúrni a RoomDatabase-ba, ha van wifi. (beszúrás már kialakítva) (wifi ellenőrzés kész)
    private void getBirdsFromFirestore(Application application) {
        if (isWifiConnected(application)) {
            mBirds.get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Bird bird = document.toObject(Bird.class);

                            if (bird != null) {
                                bird.setId(document.getId());
                                insertBird(bird);
                            } else {
                                Log.e("DATA", "--Firestore birds convertion error.--");
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("DATA", "--Firestore birds query error: --" + e + "--");
                    });
        }
    }
}
