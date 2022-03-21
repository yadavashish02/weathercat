package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class Insert(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(
		lat: Double,
		lon: Double,
		name: String,
		state: String,
		country: String,
		isCurrent: Boolean = false
	) {
		repository.insertUserCityWithWeather(
			UserCity(
				Coordinates(lat, lon),
				name, state, country,isCurrent
			)
		)
	}
}