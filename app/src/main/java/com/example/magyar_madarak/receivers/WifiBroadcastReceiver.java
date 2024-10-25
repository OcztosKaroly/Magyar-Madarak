package com.example.magyar_madarak.receivers;

import static com.example.magyar_madarak.utils.ResourceAvailabilityUtils.isWifiConnected;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.magyar_madarak.data.DataSyncWorker;

public class WifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isWifiConnected(context)) {
            OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(DataSyncWorker.class).build();
            WorkManager.getInstance(context).enqueue(syncRequest);
        }
    }
}
