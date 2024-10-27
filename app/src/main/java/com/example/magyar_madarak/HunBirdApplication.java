package com.example.magyar_madarak;

import android.app.Application;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.magyar_madarak.workers.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class HunBirdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        WorkManager.getInstance(this).cancelAllWork();

        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                24,
                TimeUnit.HOURS).addTag("daily_notification").build();
        WorkManager.getInstance(this).enqueue(dailyWorkRequest);
    }
}
