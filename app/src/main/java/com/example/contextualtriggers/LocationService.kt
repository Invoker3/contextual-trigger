package com.example.contextualtriggers

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import kotlin.math.roundToInt

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationServiceInterface
    private lateinit var weatherHistory:String
    private var speed = 0.0
    private var flag = 0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = LocationServiceImpl(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {

        val locationNotification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Fitness Service")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)

        val locationNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        weatherHistory=""

        locationClient
            .getLocationUpdates(0L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

                weatherUpdateNotifications(location, locationNotification, locationNotificationManager)
            }
            .launchIn(serviceScope)

        startForeground(1, locationNotification.build())
    }

    fun weatherUpdateNotifications(location: Location,locationNotification : NotificationCompat.Builder,
                                   locationNotificationManager:NotificationManager)
    {
        val url = WeatherAPIURL(location)
        val apiResponse = URL(url).readText()
        val jsonObject = JSONTokener(apiResponse).nextValue() as JSONObject
        val jsonArray = jsonObject.getJSONArray("weather")
        var weather = ""

        val wind = jsonObject.getJSONObject("wind")
        speed = wind.getDouble("speed") + 10

        for (i in 0 until jsonArray.length()) {
            weather = jsonArray.getJSONObject(i).getString("description")
        }

        if (weatherHistory == "") {
            weatherHistory = weather
            flag = 1
        }
        if (weatherHistory != weather || flag == 1) {
            if (weather.contains("rain")) {
                val updatedNotification = locationNotification.setContentText(
                    "It's a rainy day, please prefer to walk indoors"
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else if ((weather.contains("sunny") ||
                        weather.contains("clear"))
                && checkWind(speed.roundToInt()) == "NOT_WINDY"
            ) {
                val updatedNotification = locationNotification.setContentText(
                    "The sun is shining and it's the perfect time to take a walk around!"
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else if ((weather.contains("sunny") ||
                        weather.contains("clear"))
                && checkWind(speed.roundToInt()) == "WINDY") {
                val updatedNotification = locationNotification.setContentText(
                    "The sun is shining but it's too windy prefer to walk indoors!"
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else if (weather.contains("snow")) {
                val updatedNotification = locationNotification.setContentText(
                    "It's snowing outside, please prefer to walk indoors."
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else if (weather.contains("clouds")
                && checkWind(speed.roundToInt()) == "WINDY") {
                val updatedNotification = locationNotification.setContentText(
                    "It's a cloudy and windy day, please prefer to walk indoors."
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else if (weather.contains("clouds")
                && checkWind(speed.roundToInt()) == "NOT_WINDY") {
                val updatedNotification = locationNotification.setContentText(
                    "It's a cloudy day, you can walk outside but please carry your jacket!"
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            } else {
                val updatedNotification = locationNotification.setContentText(
                    "Weather is unpredictable, please prefer to walk indoors"
                )
                locationNotificationManager.notify(1, updatedNotification.build())
            }
            flag = 0
        }
    }

    fun WeatherAPIURL(location:Location) : String{
        return "https://api.openweathermap.org/data/2.5/weather?lat=" + location.latitude.toString() +
                "&lon=" + location.longitude.toString() + Constants.API_KEY
    }

    fun checkWind(wind_speed:Int) : String{
        return if( wind_speed <= 11 ){
            "NOT_WINDY"
        } else {
            "WINDY"
        }
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