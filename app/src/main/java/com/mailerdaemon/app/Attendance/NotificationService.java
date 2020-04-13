package com.mailerdaemon.app.Attendance;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mailerdaemon.app.LoginActivity;
import com.mailerdaemon.app.R;

import java.util.Calendar;

public class NotificationService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Intent intent1 = new Intent(this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "id123")
                .setContentTitle("Reminder")
                .setSmallIcon(R.drawable.ic_noti)
                .setContentText("Mark your today's attendance")
                .setSubText("click to open")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSound(uri)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(Calendar.getInstance().get(Calendar.MINUTE), builder.build());
    }
}
