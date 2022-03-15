package com.hitmeows.weathercat.features.search.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import com.hitmeows.weathercat.features.search.use_cases.GetCity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class SearchScreenViewModel(
	private val getCity: GetCity
): ViewModel() {
	private val _citiesState = mutableStateOf(SearchedCitiesState())
	val citiesState: State<SearchedCitiesState> = _citiesState
	
	private var searchJob: Job? = null
	fun searchCity(cityName: String) {
		searchJob?.cancel()
		searchJob = viewModelScope.launch{
			getCity(cityName).collectLatest {
				when(it) {
					is Resource.Loading -> {
						_citiesState.value = SearchedCitiesState(isLoading = true)
					}
					is Resource.Error -> {
						_citiesState.value = SearchedCitiesState(
							isError = true,
							errorMessage = it.exception?.message?:"unknown error occurred"
						)
					}
					else -> {
						val list = it.data
						if (list.isNullOrEmpty()) {
							_citiesState.value = SearchedCitiesState(
								isError = true,
								errorMessage = "No such city found"
							)
						} else {
							_citiesState.value = SearchedCitiesState(citiesList = it.data)
						}
					}
				}
			}
		}
	}
	
	fun getWeather(lat: Double, lon: Double) {
		//todo
	}
}