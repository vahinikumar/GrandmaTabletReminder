package com.example.grandmatabletreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);

        textView.setText("Grandma Tablet Reminder\n\n7:30 AM alarm is scheduled.");
        textView.setTextSize(24);
        textView.setGravity(android.view.Gravity.CENTER);

        setContentView(textView);

        scheduleAlarm();
    }

    private void scheduleAlarm() {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }
}
