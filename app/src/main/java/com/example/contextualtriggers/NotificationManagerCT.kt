package com.example.contextualtriggers

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class NotificationManagerCT : Service() {

    fun buildNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, "location")
            .setContentTitle("Fitness Service")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
    }

    fun notificationManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}