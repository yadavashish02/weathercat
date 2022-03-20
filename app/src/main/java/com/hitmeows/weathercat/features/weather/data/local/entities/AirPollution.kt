package com.hitmeows.weathercat.features.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Entity
data class AirPollution(
	@PrimaryKey(autoGenerate = false)
	val coordinates: Coordinates,
	val aqi: Int
)
