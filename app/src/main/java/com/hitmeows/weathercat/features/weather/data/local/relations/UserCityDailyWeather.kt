package com.hitmeows.weathercat.features.weather.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hitmeows.weathercat.features.weather.data.local.entities.DailyWeather
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity

data class UserCityDailyWeather(
	@Embedded
	val userCity: UserCity,
	@Relation(
		parentColumn = "coordinates",
		entityColumn = "coordinates"
	)
	val dailyWeather: List<DailyWeather>
)
