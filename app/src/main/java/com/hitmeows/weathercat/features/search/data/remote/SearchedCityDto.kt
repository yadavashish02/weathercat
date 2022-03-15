package com.hitmeows.weathercat.features.search.data.remote

import com.hitmeows.weathercat.features.country.CountryNameFromIsoCode
import com.hitmeows.weathercat.features.country.data.local.CountryDao
import com.hitmeows.weathercat.features.country.data.local.CountryDatabase
import com.hitmeows.weathercat.features.search.domain.SearchedCity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchedCityDto(
	@SerialName("name")
	val cityName: String,
	@SerialName("state")
	val stateName: String? = null,
	@SerialName("country")
	val countryCode: String,
	val lat: Double,
	val lon: Double
){
	suspend fun toSearchedCity(dao: CountryDao): SearchedCity {
		return SearchedCity(
			cityName,
			stateName?:"",
			CountryNameFromIsoCode(dao).invoke(countryCode),
			lat,
			lon
		)
	}
}
