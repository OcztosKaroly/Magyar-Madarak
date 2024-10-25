package com.example.magyar_madarak.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.magyar_madarak.data.dao.BirdDAO;
import com.example.magyar_madarak.data.dao.ObservationDAO;
import com.example.magyar_madarak.data.dao.UserDAO;
import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.data.model.Observation;
import com.example.magyar_madarak.data.model.User;
import com.example.magyar_madarak.utils.ConverterUtils;

@Database(entities = {Bird.class, User.class, Observation.class}, version = 3, exportSchema = false)
@TypeConverters({ConverterUtils.class})
public abstract class HunBirdsRoomDatabase extends RoomDatabase {
    private static HunBirdsRoomDatabase instance;

    public abstract BirdDAO birdDao();
    public abstract UserDAO userDAO();
    public abstract ObservationDAO observationDAO();

    public static HunBirdsRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (HunBirdsRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    HunBirdsRoomDatabase.class,
                                    "hun_birds_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
