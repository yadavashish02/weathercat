package com.hitmeows.weathercat.features.weather.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hitmeows.weathercat.features.weather.data.local.entities.CurrentWeather
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity

data class UserCityCurrentWeather(
	@Embedded
	val userCity: UserCity,
	@Relation(
		parentColumn = "coordinates",
		entityColumn = "coordinates"
	)
	val currentWeather: CurrentWeather
)
