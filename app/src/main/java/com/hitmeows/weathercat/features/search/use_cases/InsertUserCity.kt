package com.hitmeows.weathercat.features.search.use_cases

import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.search.domain.SearchedCity
import com.hitmeows.weathercat.features.weather.use_cases.Insert

class InsertUserCity(
	private val insert: Insert
) {
	suspend operator fun invoke(searchedCity: SearchedCity): Resource<Boolean> {
		return try {
			insert.invoke(
				searchedCity.lat,
				searchedCity.lon,
				searchedCity.cityName,
				searchedCity.stateName,
				searchedCity.countryName
			)
			Resource.Success(true)
		} catch (e: Exception) {
			Resource.Error(e, false)
		}
		
	}
}