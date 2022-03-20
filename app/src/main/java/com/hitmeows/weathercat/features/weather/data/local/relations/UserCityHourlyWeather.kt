package com.hitmeows.weathercat.features.weather.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hitmeows.weathercat.features.weather.data.local.entities.HourlyWeather
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity

data class UserCityHourlyWeather(
	@Embedded
	val userCity: UserCity,
	@Relation(
		parentColumn = "coordinates",
		entityColumn = "coordinates"
	)
	val hourlyWeather: List<HourlyWeather>
)
