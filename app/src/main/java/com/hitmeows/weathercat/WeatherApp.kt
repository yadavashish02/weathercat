package com.hitmeows.weathercat

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application()