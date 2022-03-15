package com.hitmeows.weathercat.features.search.domain


data class SearchedCity(
	val cityName: String,
	val stateName: String,
	val countryName: String,
	val lat: Double,
	val lon: Double
)
