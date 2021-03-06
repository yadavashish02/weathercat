package com.hitmeows.weathercat.features.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Entity
data class HourlyWeather(
	val coordinates: Coordinates,
	val temp: Int,
	val weatherIcon: String,
	val dt: Long,
	val timezone: Long,
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0
)
