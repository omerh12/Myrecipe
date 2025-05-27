package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Locale;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnSetAlarm;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarm);

        // Adjust window insets for immersive UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(this);

        // Get AlarmManager system service
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create the notification channel
        createNotificationChannel();
    }

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSetAlarm) {
            // Build MaterialTimePicker with keyboard input mode

            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H) // You can also use CLOCK_12H
                    .setHour(12) // Default hour shown
                    .setMinute(0) // Default minute shown
                    .setTitleText("Select Alarm Time")
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD) // Allow manual typing of time
                    .build();

            // Show the picker
            picker.show(getSupportFragmentManager(), "ALARM_TIME_PICKER");

            // Set action when user clicks OK
            picker.addOnPositiveButtonClickListener(v -> {
                int hour = picker.getHour();
                int minute = picker.getMinute();

                // Set the calendar time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // Create intent to trigger AlarmReceiver
                Intent intent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE // Required for newer Android versions
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                // Schedule the alarm
                //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                // Show confirmation
                Toast.makeText(this,
                        "Alarm set for " + String.format("%02d:%02d", hour, minute),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Create a high importance notification channel
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                "ALARM_CHANNEL_ID",
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);





//    public void onClick(View view) {
//        if(view.getId() == R.id.btnSetAlarm) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, tpTimeSelect.getHour());
//            calendar.set(Calendar.MINUTE, tpTimeSelect.getMinute());
//            calendar.set(Calendar.SECOND, 0);
//
//            Intent intent = new Intent(this, AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
//
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void createNotificationChannel() {
//        NotificationChannel channel = new NotificationChannel(
//                "ALARM_CHANNEL_ID", "Alarm Channel", NotificationManager.IMPORTANCE_HIGH);
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(channel);
    }

}