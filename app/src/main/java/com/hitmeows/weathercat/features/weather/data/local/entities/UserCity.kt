package com.hitmeows.weathercat.features.weather.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Entity
data class UserCity(
	@PrimaryKey(autoGenerate = false)
	val coordinates: Coordinates,
	val name: String,
	val state: String,
	val country: String,
	val isCurrent: Boolean
)


