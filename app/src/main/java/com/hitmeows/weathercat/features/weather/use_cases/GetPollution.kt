package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.AirPollution
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class GetPollution(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(lat: Double, lon: Double): AirPollution {
		return repository.getAirPollution(Coordinates(lat, lon))
	}
}