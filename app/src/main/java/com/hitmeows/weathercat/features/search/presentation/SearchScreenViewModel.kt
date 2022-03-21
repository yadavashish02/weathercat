package com.hitmeows.weathercat.features.search.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.search.domain.SearchedCity
import com.hitmeows.weathercat.features.search.use_cases.SearchUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
	private val useCases: SearchUseCases
) : ViewModel() {
	private val _citiesState = mutableStateOf(SearchedCitiesState())
	val citiesState: State<SearchedCitiesState> = _citiesState
	private val _errorState = mutableStateOf("")
	val errorState: State<String> = _errorState
	
	private var searchJob: Job? = null
	private var insertJob: Job? = null
	private var insertCurrentJob: Job? = null
	
	fun searchCity(cityName: String) {
		searchJob?.cancel()
		searchJob = viewModelScope.launch {
			useCases.getCity(cityName).collectLatest {
				when (it) {
					is Resource.Loading -> {
						_citiesState.value = SearchedCitiesState(isLoading = true)
					}
					is Resource.Error -> {
						_citiesState.value = SearchedCitiesState(
							isError = true,
							errorMessage = it.exception?.message ?: "unknown error occurred"
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
	
	
	fun insertWeather(lat: Double, lon: Double) {
		Log.d("okhttp","$lat,$lon")
		if (insertJob?.isCompleted == true || insertJob == null) {
			insertCurrentJob = viewModelScope.launch(Dispatchers.IO) {
				useCases.insertCurrentUserCity.invoke(lat, lon)
			}
		}
	}
	
	fun insertWeather(searchedCity: SearchedCity) {
		if (insertJob?.isCompleted == true || insertJob == null) {
			insertJob = viewModelScope.launch(Dispatchers.IO) {
				when (val v = useCases.insertUserCity.invoke(searchedCity)) {
					is Resource.Error -> _errorState.value = v.exception?.localizedMessage?:""
					else -> _errorState.value = ""
				}
			}
		}
		
	}
}