package com.hitmeows.weathercat.features.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Entity
data class CurrentWeather(
	@PrimaryKey(autoGenerate = false)
	val coordinates: Coordinates,
	val currentTemp: Int,
	val maxTemp: Int,
	val minTemp: Int,
	val weatherId: Int,
	val weatherDescription: String,
	val feelsLike: Int,
	val pressure: Double,
	val humidity: Double,
	val windSpeed: Double,
	val windDirection: Double,
	val dt: Long,
	val timezone: Long
)
