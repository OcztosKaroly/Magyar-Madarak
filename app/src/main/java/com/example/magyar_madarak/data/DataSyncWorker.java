package com.example.magyar_madarak.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.util.Log;

public class DataSyncWorker extends Worker {

    public DataSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        syncDataWithServer();

        return Result.success();
    }

    private void syncDataWithServer() {
        // TODO: Firebase és RoomDB szinkronizálása
    }
}
