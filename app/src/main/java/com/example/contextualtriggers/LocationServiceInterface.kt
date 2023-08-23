package com.example.contextualtriggers

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationServiceInterface {

    fun getLocationUpdates(interval: Long): Flow<Location>
    class LocationException(message: String): Exception()
}