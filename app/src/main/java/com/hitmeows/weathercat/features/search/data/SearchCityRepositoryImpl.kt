package com.hitmeows.weathercat.features.search.data

import com.hitmeows.weathercat.features.country.data.local.CountryDao
import com.hitmeows.weathercat.features.search.data.remote.SearchApi
import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import com.hitmeows.weathercat.features.search.domain.SearchedCity

class SearchCityRepositoryImpl(
	private val dao: CountryDao,
	private val api: SearchApi
) : SearchCityRepository {
	override suspend fun getCity(query: String): List<SearchedCity> {
		return api.searchCity(query).map { it.toSearchedCity(dao) }
	}
	
	override suspend fun getCityByCoord(lat: Double, lon: Double): SearchedCity {
		val city = api.reverseSearch(lat, lon)[0]
		return SearchedCity(
			city.name,
			city.state ?: "",
			city.country,
			lat, lon
		)
	}
}