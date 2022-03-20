package com.hitmeows.weathercat.features.weather.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.hitmeows.weathercat.features.weather.data.local.entities.AirPollution
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity

data class UserCityAirPollution(
	@Embedded
	val userCity: UserCity,
	@Relation(
		parentColumn = "coordinates",
		entityColumn = "coordinates"
	)
	val airPollution: AirPollution
)
