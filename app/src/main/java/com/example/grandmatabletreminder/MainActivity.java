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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setTextSize(24);
        textView.setGravity(android.view.Gravity.CENTER);
        setContentView(textView);

        showReceiverStatus(textView);
        scheduleAlarm(textView);
    }

    private void showReceiverStatus(TextView textView) {

        long firedTime = getSharedPreferences(
                "alarm_test",
                MODE_PRIVATE
        ).getLong("receiver_fired", 0);

        if (firedTime == 0) {

            textView.setText(
                    "Receiver status:\n\n" +
                    "NOT FIRED YET"
            );

        } else {

            String time = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss",
                    Locale.getDefault()
            ).format(new Date(firedTime));

            textView.setText(
                    "Receiver status:\n\n" +
                    "FIRED!\n\n" +
                    "Time: " + time
            );
        }
    }

    private void scheduleAlarm(TextView textView) {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager == null) {
            textView.setText("ERROR:\nAlarmManager is null");
            return;
        }

        if (!alarmManager.canScheduleExactAlarms()) {

            textView.setText(
                    "ERROR:\nExact alarm permission is NOT allowed"
            );

            Intent intent = new Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            );

            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();

        // TEST: 2 minutes from now
        calendar.add(Calendar.MINUTE, 2);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        try {

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            textView.setText(
                    "ALARM SCHEDULED\n\n" +
                    new SimpleDateFormat(
                            "HH:mm:ss",
                            Locale.getDefault()
                    ).format(calendar.getTime()) +
                    "\n\nOpen this app AFTER the alarm time."
            );

        } catch (Exception e) {

            textView.setText(
                    "ALARM ERROR:\n\n" +
                    e.getClass().getSimpleName() +
                    "\n\n" +
                    e.getMessage()
            );
        }
    }
}
