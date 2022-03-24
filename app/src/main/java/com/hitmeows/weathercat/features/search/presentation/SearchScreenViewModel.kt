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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
	private val _isLoading = MutableStateFlow(false)
	val isLoading: StateFlow<Boolean>
		get() = _isLoading.asStateFlow()
	
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
		Log.d("okhttp", "$lat,$lon")
		if (insertCurrentJob?.isCompleted == true || insertCurrentJob == null) {
			insertCurrentJob = viewModelScope.launch(Dispatchers.IO) {
				_isLoading.emit(true)
				Log.d("okhttp", "iini")
				try {
					useCases.insertCurrentUserCity.invoke(lat, lon)
				} catch (e: Exception) {
					Log.d("error", e.message ?: "err")
				}
				_isLoading.emit(false)
			}
		}
	}
	
	fun insertWeather(searchedCity: SearchedCity) {
		if (insertJob?.isCompleted == true || insertJob == null) {
			insertJob = viewModelScope.launch(Dispatchers.IO) {
				_isLoading.emit(true)
				when (val v = useCases.insertUserCity.invoke(searchedCity)) {
					is Resource.Error -> _errorState.value = v.exception?.localizedMessage ?: ""
					else -> _errorState.value = ""
				}
				_isLoading.emit(false)
			}
		}
		
	}
}