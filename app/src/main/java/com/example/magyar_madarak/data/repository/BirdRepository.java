package com.example.magyar_madarak.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.dao.BirdDAO;
import com.example.magyar_madarak.data.database.BirdRoomDatabase;
import com.example.magyar_madarak.data.model.Bird;

import java.util.List;

public class BirdRepository {
    private BirdDAO birdDAO;

    private LiveData<List<Bird>> birds;

    public BirdRepository(Application application) {
        BirdRoomDatabase db = BirdRoomDatabase.getInstance(application);
        birdDAO = db.birdDao();
        birds = birdDAO.getAllBirds();
    }

    public LiveData<List<Bird>> getAllBirds() {
        return birds;
    }

    public LiveData<Bird> getBirdByName(String birdName) {
        return birdDAO.getBirdByName(birdName);
    }
}
