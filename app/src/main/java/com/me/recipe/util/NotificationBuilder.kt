package com.me.recipe.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.me.recipe.R
import com.me.recipe.core.utils.TAG
import com.me.recipe.util.extention.encodeToUtf8
import timber.log.Timber

object NotificationBuilder {

    private val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
    private const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
    private const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
    private const val NOTIFICATION_ID = 1

    fun showNotification(id: Int, title: String, message: String, banner: String, context: Context) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("recipe://composables.com/$id/$title/${banner.encodeToUtf8()}"),
        )
        val activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setContentIntent(activity)
            .setAutoCancel(true)

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.tag(TAG).e("NO PERMISSION TO SHOW NOTIFICATION")
            return
        }

        if (banner.isNotEmpty()) {
            val loader = ImageLoader(context)
            val req = ImageRequest.Builder(context)
                .data(banner)
                .target { result ->
                    val bitmap = (result as BitmapDrawable).bitmap
//                    builder.setLargeIcon(bitmap)
                    builder.setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(bitmap),
                    )
                    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
                }
                .build()
            loader.enqueue(req)
            return
        }

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
