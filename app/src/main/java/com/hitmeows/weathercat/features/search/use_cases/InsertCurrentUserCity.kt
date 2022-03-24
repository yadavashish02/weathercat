package com.hitmeows.weathercat.features.search.use_cases

import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import com.hitmeows.weathercat.features.weather.use_cases.Insert
import kotlinx.coroutines.delay

class InsertCurrentUserCity(
	private val repository: SearchCityRepository,
	private val insert: Insert
) {
	suspend operator fun invoke(lat: Double, lon: Double) {
		val city = repository.getCityByCoord(lat, lon)
		delay(2000)
		insert.invoke(
			city.lat,
			city.lon,
			city.cityName,
			city.stateName,
			city.countryName,
			true
		)
	}
}