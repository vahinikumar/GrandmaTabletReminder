package com.example.grandmatabletreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView title;
    private TextView message;
    private Button takenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showReminderScreen();

        if (savedInstanceState == null) {
            scheduleAlarm();
        }
    }

    private void showReminderScreen() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);

        title = new TextView(this);
        title.setText("💊 TABLET TIME");
        title.setTextSize(40);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);

        message = new TextView(this);
        message.setText(
                "Please take your tablet"
        );
        message.setTextSize(28);
        message.setGravity(Gravity.CENTER);
        message.setPadding(0, 40, 0, 60);

        takenButton = new Button(this);
        takenButton.setText("I TOOK IT");
        takenButton.setTextSize(32);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        180
                );

        takenButton.setLayoutParams(buttonParams);

        takenButton.setOnClickListener(v -> {

            title.setText("✓ TABLET TAKEN");
            message.setText("Thank you!");

            takenButton.setEnabled(false);
        });

        layout.addView(title);
        layout.addView(message);
        layout.addView(takenButton);

        setContentView(layout);
    }

    private void scheduleAlarm() {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager == null) {
            return;
        }

        if (!alarmManager.canScheduleExactAlarms()) {

            Intent permissionIntent = new Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            );

            startActivity(permissionIntent);
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

        // TEMPORARY TEST:
        // 2 minutes from now
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
    }
}
