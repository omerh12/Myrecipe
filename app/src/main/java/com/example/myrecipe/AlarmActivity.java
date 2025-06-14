package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnAlarmSetAlarm;// כפתור הגדרת שעון מעורר
    AlarmManager alarmManager;// משתנה של מנהל ההתראות

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnAlarmSetAlarm = findViewById(R.id.btnAlarmSetAlarm);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);// מקבל את שירות השעונים מהמערכת
        btnAlarmSetAlarm.setOnClickListener(this);
        createNotificationChannel();// יוצר ערוץ התראות לשימוש עתידי
    }

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAlarmSetAlarm) {


            MaterialTimePicker picker = new MaterialTimePicker.Builder()// יוצר שדה לבחירת שעה
                    .setTimeFormat(TimeFormat.CLOCK_24H) // קובע פורמט 24 שעות
                    .setHour(12)// ברירת מחדל שעה 12
                    .setMinute(0)// ברירת מחדל דקה 0
                    .setTitleText("Select Alarm Time") // כותרת לחלון
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)// מאפשר להקליד את השעה
                    .build();

            picker.show(getSupportFragmentManager(), "ALARM_TIME_PICKER");// מציג את חלון הבחירה

            picker.addOnPositiveButtonClickListener(v -> {// פעולה שמתרחשת כשהמשתמש לוחץ על "אישור"
                int hour = picker.getHour();// השעה שנבחרה
                int minute = picker.getMinute();// דקה שנבחרה

                Calendar calendar = Calendar.getInstance();// הזמן נוכחי
                calendar.set(Calendar.HOUR_OF_DAY, hour);// מגדיר את השעה הרצויה
                calendar.set(Calendar.MINUTE, minute);// מגדיר את הדקה הרצויה
                calendar.set(Calendar.SECOND, 0);// אפס שניות
                calendar.set(Calendar.MILLISECOND, 0);// אפס אלפיות

                Intent intent = new Intent(this, AlarmReceiver.class);// יוצר אינטנט שיפעיל את AlarmReceiver
                PendingIntent pendingIntent = PendingIntent.getBroadcast(// יוצר PendingIntent שישוגר בשעה שנקבעה
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_MUTABLE
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// אם גרסת האנדרואיד היא M ומעלה
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);// קובע שעון מדויק שפועל גם במצב שינה
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);// עבור גרסאות ישנות - רק "מדויק"
                }

                Toast.makeText(this,
                        "Alarm set for " + String.format("%02d:%02d", hour, minute),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }


    private void createNotificationChannel() {// פעולה שיוצרת ערוץ התראות
        NotificationChannel channel = new NotificationChannel(// יוצר ערוץ חדש
                "ALARM_CHANNEL_ID",// מזהה לערוץ
                "Alarm Channel",// שם הערוץ
                NotificationManager.IMPORTANCE_HIGH// רמת החשיבות של ההתראות בערוץ זה
        );
        NotificationManager manager = getSystemService(NotificationManager.class);// מקבל את מנהל ההתראות
        manager.createNotificationChannel(channel);// יוצר את הערוץ בפועל

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_about) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_favorites_list) {
            Intent intent = new Intent(this, FavoritesListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_recipe_list) {
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_upload_new_recipe) {
            Intent intent = new Intent(this, UploadNewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_alarm) {
            return true;
        } else if (itemId == R.id.menu_item_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.remove("user_email");
            editor.remove("user_token");
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, FirstScreenActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}