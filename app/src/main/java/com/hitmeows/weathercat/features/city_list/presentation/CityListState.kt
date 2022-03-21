package com.hitmeows.weathercat.features.city_list.presentation

import com.hitmeows.weathercat.features.city_list.UserCityWithWeather

data class CityListState(
	val isLoading: Boolean = false,
	val isError: Boolean = false,
	val errorMessage: String = "",
	val cityList: List<UserCityWithWeather> = emptyList()
)
