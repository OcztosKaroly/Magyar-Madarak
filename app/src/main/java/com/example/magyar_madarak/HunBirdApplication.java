package com.example.magyar_madarak;

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

        if (!isRunningNotificationByTag("daily_notification")) {
            PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
                    NotificationWorker.class,
                    24,
                    TimeUnit.HOURS).addTag("daily_notification").build();
            WorkManager.getInstance(this).enqueue(dailyWorkRequest);
        }
    }

    private boolean isRunningNotificationByTag(String tag) {
        ListenableFuture<List<WorkInfo>> future = WorkManager.getInstance(this).getWorkInfosByTag(tag);
        try {
            List<WorkInfo> workInfos = future.get();
            for (WorkInfo workInfo : workInfos) {
                if (workInfo.getState() == WorkInfo.State.RUNNING || workInfo.getState() == WorkInfo.State.ENQUEUED) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e("NOTIFICATION", "--Error occurred when tried to get running notifications.--", e);
        }
        return false;
    }
}
