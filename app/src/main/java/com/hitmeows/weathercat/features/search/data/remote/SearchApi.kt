package com.hitmeows.weathercat.features.search.data.remote

interface SearchApi {
	suspend fun searchCity(query: String): List<SearchedCityDto>
}