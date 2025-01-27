package com.example.magyar_madarak.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.ui.KnowledgeBaseActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "daily_hun_birds_notification_channel";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("notifications", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("dailyNotificationEnabled", true)) {
            createNotificationChannel();
            sendNotification();
        }
        return Result.success();
    }

    private void sendNotification() {
        List<String> notificationMessages = Arrays.asList(
                "A széncinege hangja az erdő szíve!",
                "Figyeld meg ma egy madár viselkedését, és jegyezd fel!",
                "A legszebb madarak nem mindig a legszínesebbek!",
                "Tölts egy percet a természet csodáival!",
                "Próbáld ki a madárhatározó funkciót!",
                "Rögzítsd élményeidet, rögzítsd megfigyeléseidet!"
        );

        Random random = new Random();
        String randomMessage = notificationMessages.get(random.nextInt(notificationMessages.size()));

        Intent intent = new Intent(getApplicationContext(), KnowledgeBaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                641,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bird_identification)
                .setContentTitle("Magyarországi Madarak")
                .setContentText(randomMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        CharSequence name = "Napi értesítő";
        String description = "Napi értesítő a Magyar Madarak alkalmazás használásának ösztönzésére.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}