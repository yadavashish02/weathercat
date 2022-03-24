package com.hitmeows.weathercat.features.city_list

import com.hitmeows.weathercat.features.weather.data.local.Coordinates

data class UserCityWithWeather(
	val name: String,
	val weatherId: Int,
	val weatherDescription: String,
	val temp: Int,
	val coordinates: Coordinates
)
