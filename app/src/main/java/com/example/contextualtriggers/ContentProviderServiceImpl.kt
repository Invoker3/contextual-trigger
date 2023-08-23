package com.example.contextualtriggers

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.*

class ContentProviderServiceImpl(
    private val context: Context
) : ContentProviderServiceInterface {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingPermission")
    override fun getContentProviderData(): Boolean {
        if (!context.hasNotificationPermission()) {
            throw ContentProviderServiceInterface.NotificationException("Missing notification permission")
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val isNotificationEnabled = notificationManager.areNotificationsEnabled()

        if (!isNotificationEnabled) {
            throw ContentProviderServiceInterface.NotificationException("Missing notification permission")
        }
        return isNotificationEnabled
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun Context.hasNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun setTimeTrigger() {
        createNotificationChannel()
        scheduleNotification(8,0,"Good morning! Time to rise and shine, and make your health a priority. ")
        scheduleNotification(13,0,"Good afternoon! Remember to prioritize your fitness today. Even small efforts can make a big difference.")
        scheduleNotification(18,0,"Good evening! Keep up the fitness momentum and finish the day strong!")
    }

    private fun getTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun scheduleNotification(hour: Int, minute: Int, message: String) {
        var notificationID = 1
        val titleExtra = "titleExtra"
        val messageExtra = "messageExtra"
        val intent = Intent(context.applicationContext, Notification::class.java)
        val title = "Fitness Service"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationID++

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime(hour, minute)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "Notify the user"
        val channelID = "channel1"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}