package com.hitmeows.weathercat.features.weather.use_cases

data class WeatherUseCases(
	val getCurrent: GetCurrent,
	val getAllUserCities: GetAllUserCities,
	val insert: Insert
)
