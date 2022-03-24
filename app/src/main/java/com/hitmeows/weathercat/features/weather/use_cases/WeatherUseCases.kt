package com.hitmeows.weathercat.features.weather.use_cases

data class WeatherUseCases(
	val getCurrent: GetCurrent,
	val getAllUserCities: GetAllUserCities,
	val insert: Insert,
	val getPollution: GetPollution,
	val getHourly: GetHourly,
	val getDaily: GetDaily,
	val deleteUserCity: DeleteUserCity
)
