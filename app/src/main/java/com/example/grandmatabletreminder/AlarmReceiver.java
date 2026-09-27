package com.example.grandmatabletreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Record that the receiver fired
        context.getSharedPreferences(
                "alarm_test",
                Context.MODE_PRIVATE
        ).edit()
                .putLong(
                        "receiver_fired",
                        System.currentTimeMillis()
                )
                .apply();

        // Try to open the reminder screen
        Intent activityIntent = new Intent(
                context,
                MainActivity.class
        );

        activityIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        context.startActivity(activityIntent);
    }
}
