package com.hitmeows.weathercat.features.search.data.remote

interface SearchApi {
	suspend fun searchCity(query: String): List<SearchedCityDto>
	suspend fun reverseSearch(lat: Double,lon: Double): List<ReverseSearchDto>
}