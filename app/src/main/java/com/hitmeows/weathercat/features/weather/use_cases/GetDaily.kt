package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class GetDaily(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(lat: Double, lon: Double) =
		repository.getDailyWeather(Coordinates(lat, lon))
}