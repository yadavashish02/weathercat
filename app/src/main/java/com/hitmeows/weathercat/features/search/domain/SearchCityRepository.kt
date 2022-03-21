package com.hitmeows.weathercat.features.search.domain

interface SearchCityRepository {
	suspend fun getCity(query: String): List<SearchedCity>
	suspend fun getCityByCoord(lat: Double, lon: Double): SearchedCity
}