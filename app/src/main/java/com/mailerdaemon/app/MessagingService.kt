package com.mailerdaemon.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("NEW_TOKEN", s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MESSAGE_RECEIVED", remoteMessage.toString())
        val title = remoteMessage.notification!!.title
        val message = remoteMessage.notification!!.body
        val clickAction = remoteMessage.notification!!.clickAction
        val intent = Intent(clickAction)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = "id123"
        val channelName = "MESSAGE"
        val manager = NotificationManagerCompat.from(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setSubText("click to open")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(uri)
            .setAutoCancel(true)
        if (remoteMessage.notification!!.imageUrl != null) {
            val bitmap = getBitmapfromUrl(remoteMessage.notification!!.imageUrl.toString())
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null)
            ).setLargeIcon(bitmap)
        }
        manager.notify(Calendar.getInstance()[Calendar.MINUTE], builder.build())
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("image url", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }
}
