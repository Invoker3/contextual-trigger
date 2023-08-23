package com.example.contextualtriggers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class ContextTriggerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val locationChannel = NotificationChannel(
            "location",
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )
        val contentProviderChannel = NotificationChannel(
            "content",
            "Content",
            NotificationManager.IMPORTANCE_LOW
        )
        val timeChannel = NotificationChannel(
            "time",
            "Time",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(locationChannel)
        notificationManager.createNotificationChannel(contentProviderChannel)
        notificationManager.createNotificationChannel(timeChannel)
    }
}