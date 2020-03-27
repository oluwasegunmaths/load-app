package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"
private const val NOTIFICATION_ID = 11
const val DOWNLOAD_DESCRIPTION = "download description"


fun sendNotification(context: Context, downloadDescription: String) {

    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // We need to create a NotificationChannel associated with our CHANNEL_ID before sending a notification.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val name = context.getString(R.string.app_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    val contentIntent = Intent(context, DetailActivity::class.java)
    contentIntent.putExtra(DOWNLOAD_DESCRIPTION, downloadDescription)

    val notificationPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val downloadImage = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.baseline_cloud_download_black_48
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(downloadImage)
//    build the notification object with the data to be shown
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.baseline_cloud_download_black_48)
        .setContentTitle(context.getString(R.string.successfully_downloaded))
        .setContentText(downloadDescription)
        .setStyle(bigPicStyle)
        .setColor(context.getColor(R.color.colorPrimaryDark))
        .setLargeIcon(downloadImage)

        .addAction(
            R.drawable.baseline_cloud_download_black_48,
            context.getString(R.string.view),
            notificationPendingIntent
        )
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

        .build()

    notificationManager.notify(NOTIFICATION_ID, notification)
}

fun cancelNotifications(context: Context) {
    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}
