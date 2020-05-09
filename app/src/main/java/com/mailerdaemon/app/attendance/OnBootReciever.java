package com.mailerdaemon.app.attendance;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mailerdaemon.app.ConstantsKt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class OnBootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean x= context.getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getBoolean("Notification",false);
        if(Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED") &&x){
            Calendar calendar=Calendar.getInstance();
            long time =context.getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getLong(ConstantsKt.TIME_NOTI,0);
            calendar.setTimeInMillis(time);
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(context, NotificationReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 123, intent1,PendingIntent.FLAG_UPDATE_CURRENT );
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            String val1  = sdf.format(new Date(System.currentTimeMillis()));
            String val2  = sdf.format(calendar.getTime());

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = sdf.parse(val1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                d2 = sdf.parse(val2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert d2 != null;
            assert d1 != null;
            long elapsed = d2.getTime() - d1.getTime();

            // Determine if the time specified is past already or not. If it is past add 24 hours so it displays the following day.
            if (elapsed < 0)
            {
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1) + elapsed, AlarmManager.INTERVAL_DAY,alarmIntent);
            }
            else
            {
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + elapsed, AlarmManager.INTERVAL_DAY, alarmIntent);
            }
        }
    }
}
