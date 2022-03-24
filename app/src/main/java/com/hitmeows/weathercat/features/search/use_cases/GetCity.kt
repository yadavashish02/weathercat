package com.hitmeows.weathercat.features.search.use_cases

import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import kotlinx.coroutines.flow.flow

class GetCity(
	private val repository: SearchCityRepository
) {
	suspend operator fun invoke(cityName: String, countryCode: String = "All") = flow {
		val query = queryBuilder(cityName, countryCode)
		emit(Resource.Loading())
		try {
			emit(Resource.Success(repository.getCity(query)))
		} catch (e: Exception) {
			emit(Resource.Error(e))
		}
	}
	
	private fun queryBuilder(cityName: String, countryCode: String): String {
		//todo
		var q = cityName
		if (countryCode != "All") q += ",$countryCode"
		return q
	}
	
	
}