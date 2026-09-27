package com.example.grandmatabletreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        long firedTime = System.currentTimeMillis();

        context.getSharedPreferences(
                "alarm_test",
                Context.MODE_PRIVATE
        ).edit()
                .putLong("receiver_fired", firedTime)
                .apply();
    }
}
