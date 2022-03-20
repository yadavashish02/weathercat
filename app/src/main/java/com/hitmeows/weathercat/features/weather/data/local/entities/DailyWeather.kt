package com.hitmeows.weathercat.features.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Entity
data class DailyWeather(
	@PrimaryKey(autoGenerate = true)
	val id: Int,
	val coordinates: Coordinates,
	val minTemp: Int,
	val maxTemp: Int,
	val weatherIcon: String,
	val weatherDescription: String,
	val dt: Long,
	val timezone: Long
)
