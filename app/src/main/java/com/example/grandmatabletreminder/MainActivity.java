package com.example.grandmatabletreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String channelId = "tablet_test";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE
                );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Tablet Reminder Test",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setSound(null, null);
            manager.createNotificationChannel(channel);
        }

        Intent activityIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                200,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        android.app.Notification notification =
                new android.app.Notification.Builder(context, channelId)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Grandma Tablet Reminder")
                        .setContentText("ALARM RECEIVER FIRED")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        manager.notify(200, notification);
    }
}
