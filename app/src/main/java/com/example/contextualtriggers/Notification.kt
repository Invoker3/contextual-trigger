package com.example.contextualtriggers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notification : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        val notificationID = 1
        val channelID = "channel1"
        val titleExtra = "titleExtra"
        val messageExtra = "messageExtra"
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)

    }
}