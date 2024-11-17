package com.example.magyar_madarak;

import static com.example.magyar_madarak.utils.CommonUtils.isRunningNotificationByTag;

import android.app.Application;
import android.util.Log;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.magyar_madarak.data.database.HunBirdsRoomDatabase;
import com.example.magyar_madarak.workers.NotificationWorker;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HunBirdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
}
