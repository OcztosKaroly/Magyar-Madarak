package com.example.magyar_madarak.utils;

import android.content.Context;
import android.util.Log;

import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public class CommonUtils {
    public static String capitalizeFirstLetter(String string) {
        return Character.toTitleCase(string.charAt(0)) + string.substring(1);
    }

    public static boolean isRunningNotificationByTag(Context context, String tag) {
        ListenableFuture<List<WorkInfo>> future = WorkManager.getInstance(context).getWorkInfosByTag(tag);
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
