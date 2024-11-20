package com.example.magyar_madarak;

import static com.example.magyar_madarak.utils.AuthUtils.logout;
import static com.example.magyar_madarak.utils.CommonUtils.isRunningNotificationByTag;

import android.app.Application;
import android.content.Context;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.workers.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class HunBirdApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();

        logout(); // TODO: Ez csak a tesztelésekhez kell a fejlesztés során!!!!!!!


        // TODO: !!!!!!!!!! Törölni az adatbázistörlőt későbbiekben !!!!!!!!!!
        this.deleteDatabase("hun_birds_database");
        HunBirdsRoomDatabase db = HunBirdsRoomDatabase.getInstance(this);

//        WorkManager.getInstance(this).cancelAllWork();

        if (!isRunningNotificationByTag(this, "daily_notification")) {
            PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
                    NotificationWorker.class,
                    23,
                    TimeUnit.HOURS).addTag("daily_notification").build();
            WorkManager.getInstance(this).enqueue(dailyWorkRequest);
        }
    }

    public static Context getAppContext() {
        return appContext;
    }
}
