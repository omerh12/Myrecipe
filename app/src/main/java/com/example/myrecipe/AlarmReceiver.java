package com.example.myrecipe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {// מתבצע כאשר מתקבלת התראה
        Intent notificationIntent = new Intent(context, AlarmActivity.class);// יוצר אינטנט לפתיחת AlarmActivity
        PendingIntent pendingIntent = PendingIntent.getActivity(// להפעילו מאוחר יותר
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARM_CHANNEL_ID")// בונה את ההתראה שתוצג
                .setSmallIcon(R.drawable.baseline_access_alarm_24)// האייקון שמופיע בהתראה
                .setContentTitle("Alarm")// הכותרת של ההתראה
                .setContentText("Your alarm is ringing!")// תוכן ההתראה
                .setPriority(NotificationCompat.PRIORITY_HIGH)// קובע שהתראה זו חשובה (מופיעה ישר)
                .setContentIntent(pendingIntent)//כשתילחץ ההתראה
                .setAutoCancel(true);//התראה תיעלם בעת לחיצה

        NotificationManager notificationManager =// קובע שהתראה זו חשובה (מופיעה מיידית)
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // מקבל את שירות ההתראות
        notificationManager.notify(1, builder.build());
    }
}
