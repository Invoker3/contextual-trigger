package com.example.contextualtriggers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.contextualtriggers.ui.theme.ContextualTriggersTheme
import com.example.contextualtriggers.ui.theme.Raleway
import java.util.*

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        setContent {
            ContextualTriggersTheme {
                Column() {
                    TopAppBar(
                        elevation = 4.dp,
                        title = {
                            Text(
                                "Contextual Triggers", style = TextStyle(
                                    fontFamily = Raleway,
                                    fontSize = 20.sp
                                )
                            )
                        },
                        backgroundColor = MaterialTheme.colors.primarySurface
                    )
                    Column() {
                        DisplayList()
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                }
            }
        }
    }

    @Composable
    fun DisplayList() {
        val permissions = listOf(
            "Location", "Notification"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(permissions) { permission ->
                    if (permission == "Location") {
                        AccessLocationService("Location")
                        Divider()
                    } else {
                        AccessContentProviderService("Content Provider")
                        Divider()
                    }
                }
            }
        }
    }

    @Composable
    private fun AccessLocationService(language: String) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text(language, modifier = Modifier.padding(15.dp))
            Button(onClick = {
                if(checkConnectivity(applicationContext)) {
                    Intent(applicationContext, LocationService::class.java).apply {
                        action = LocationService.ACTION_START
                        startService(this)
                    }
                }
                else {
                    Toast.makeText(applicationContext, "No Connectivity", Toast.LENGTH_LONG).show()
                }
            }) {
                Text(text = "On")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                Intent(applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    startService(this)
                }
            }) {
                Text(text = "Off")
            }
        }
    }

    @Composable
    private fun AccessContentProviderService(language: String) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(5.dp)) {
            Text(language, modifier = Modifier.padding(15.dp))
            Button(onClick = {
                Intent(applicationContext, ContentProviderService::class.java).apply {
                    action = ContentProviderService.ACTION_START
                    startService(this)
                }
            }) {
                Text(text = "On")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                Intent(applicationContext, ContentProviderService::class.java).apply {
                    action = ContentProviderService.ACTION_STOP
                    startService(this)
                }
            }) {
                Text(text = "Off")
            }
        }
    }

    private fun checkConnectivity(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}