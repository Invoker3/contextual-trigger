package com.example.contextualtriggers

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.*

class ContentProviderService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var contentResolver: FitnessContentResolver
    private lateinit var contentProviderClient: ContentProviderServiceInterface

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        contentResolver = FitnessContentResolver(applicationContext)
        contentProviderClient = ContentProviderServiceImpl(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun start() {

        val contentNotification = NotificationCompat.Builder(this, "content")
            .setContentTitle("Fitness Service")
            .setContentText("Content Data: null")
            .setSmallIcon(R.drawable.ic_launcher_background)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val activityList: ArrayList<Activity> = contentResolver.getActivity()
        val goalList: ArrayList<Goal> = contentResolver.getGoal()

        contentProviderClient.setTimeTrigger()

        val isNotificationEnabled: Boolean = contentProviderClient.getContentProviderData()
            if (isNotificationEnabled) {
                for (goal in goalList) {
                    for (activity in activityList) {
                        val activityStepNotification = contentNotification.setContentText(
                            "You have achieved " + activity.stepsAchieved.toString() + " steps"
                        )
                        notificationManager.notify(3, activityStepNotification.build())
                        if (goal.targetSteps > activity.stepsAchieved) {
                            val updatedNotification = contentNotification.setContentText(
                                "You have " + (goal.targetSteps - activity.stepsAchieved).toString() + " steps remaining to complete " + goal.goalName
                            )
                            notificationManager.notify(2, updatedNotification.build())
                        } else {
                            val updatedNotification = contentNotification.setContentText(
                                "Congratulations! You have completed your goal " + goal.goalName + " today of " + goal.targetSteps + " steps."
                            )
                            notificationManager.notify(2, updatedNotification.build())
                        }
                    }
                }
            }
        startForeground(1, contentNotification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}