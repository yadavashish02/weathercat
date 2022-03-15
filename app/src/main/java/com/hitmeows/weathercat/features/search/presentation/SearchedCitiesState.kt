package com.hitmeows.weathercat.features.search.presentation

import com.hitmeows.weathercat.features.search.domain.SearchedCity

data class SearchedCitiesState(
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val errorMessage: String = "",
	val citiesList: List<SearchedCity> = emptyList()
)