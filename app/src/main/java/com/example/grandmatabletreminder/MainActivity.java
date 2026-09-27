package com.example.grandmatabletreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = new TextView(this);
        textView.setTextSize(24);
        textView.setGravity(android.view.Gravity.CENTER);
        setContentView(textView);

        checkReceiver();
    }

    private void checkReceiver() {

        long firedTime = getSharedPreferences(
                "alarm_test",
                MODE_PRIVATE
        ).getLong("receiver_fired", 0);

        if (firedTime > 0) {

            String time = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()
            ).format(new Date(firedTime));

            textView.setText(
                    "ALARM RECEIVER FIRED!\n\n" +
                    "Time:\n" +
                    time
            );

        } else {

            textView.setText(
                    "NO RECEIVER EVENT RECORDED\n\n" +
                    "Tap here to schedule a test alarm."
            );

            textView.setOnClickListener(
                    v -> scheduleTestAlarm()
            );
        }
    }

    private void scheduleTestAlarm() {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager == null) {
            textView.setText("AlarmManager ERROR");
            return;
        }

        if (!alarmManager.canScheduleExactAlarms()) {

            startActivity(
                    new Intent(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    )
            );

            return;
        }

        Intent intent = new Intent(
                this,
                AlarmReceiver.class
        );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this,
                        100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, 2);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long alarmTime = calendar.getTimeInMillis();

        AlarmManager.AlarmClockInfo alarmClockInfo =
                new AlarmManager.AlarmClockInfo(
                        alarmTime,
                        pendingIntent
                );

        alarmManager.setAlarmClock(
                alarmClockInfo,
                pendingIntent
        );

        String time = new SimpleDateFormat(
                "HH:mm:ss",
                Locale.getDefault()
        ).format(calendar.getTime());

        textView.setText(
                "TEST ALARM SCHEDULED\n\n" +
                "Alarm time: " + time +
                "\n\n" +
                "Close the app and wait."
        );
    }
}
