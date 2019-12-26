package com.mailerdaemon.app.Attendance;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.JobIntentService;


public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, NotificationService.class);
        JobIntentService.enqueueWork(context,NotificationService.class,123,intent1);
    }
}
