package com.hitmeows.weathercat.features.city_list

data class UserCityWithWeather(
	val name: String,
	val weatherId: Int,
	val weatherDescription: String,
	val temp: Int
)
