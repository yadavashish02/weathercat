package com.hitmeows.weathercat.features.city_list.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.weathercat.common.DeleteUserCities
import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.common.UpdateWeather
import com.hitmeows.weathercat.features.city_list.use_cases.GetUserCitiesWithWeather
import com.hitmeows.weathercat.features.weather.data.local.Coordinates
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
class CityListViewModel @Inject constructor(
	private val getUserCitiesWithWeather: GetUserCitiesWithWeather,
	private val updateWeather: UpdateWeather,
	private val deleteUserCities: DeleteUserCities
) : ViewModel() {
	private val _state = mutableStateOf(CityListState())
	val state: State<CityListState> = _state
	private val _isRefreshing = MutableStateFlow(false)
	val isRefreshing: StateFlow<Boolean>
		get() = _isRefreshing.asStateFlow()
	private val _isDisabled = MutableStateFlow(false)
	val isDisabled: StateFlow<Boolean>
		get() = _isDisabled.asStateFlow()
	
	private val deleteList = mutableListOf<Coordinates>()
	
	init {
		getCityList()
	}
	
	private var job: Job? = null
	private var updateJob: Job? = null
	private var deleteJob: Job? = null
	
	private fun getCityList() {
		job?.cancel()
		job = viewModelScope.launch {
			getUserCitiesWithWeather.invoke().collectLatest {
				when (it) {
					is Resource.Loading -> {
						_state.value =
							CityListState(isLoading = true, cityList = it.data ?: emptyList())
					}
					is Resource.Error -> {
						_state.value = CityListState(
							isError = true,
							errorMessage = it.exception?.message ?: "unknown error occurred",
							cityList = it.data ?: emptyList()
						)
					}
					else -> {
						_state.value =
							if (it.data.isNullOrEmpty())
								CityListState(
									isError = true,
									errorMessage = "no data exists"
								)
							else CityListState(cityList = it.data)
					}
				}
			}
		}
	}
	
	fun update() {
		if (updateJob == null || (updateJob?.isCompleted == true) && deleteJob?.isCompleted == true) {
			if (updateJob?.isCompleted == true) updateJob?.cancel()
			updateJob = viewModelScope.launch(Dispatchers.IO) {
				_isRefreshing.emit(true)
				when (val u = updateWeather.invoke()) {
					is Resource.Error -> {
						Log.d("okhttp", u.exception?.message ?: "err")
					}
					else -> {
					
					}
				}
				_isRefreshing.emit(false)
			}
		}
	}
	
	fun delete() {
		if (deleteJob == null || (deleteJob?.isCompleted == true && updateJob?.isCompleted == true)) {
			deleteJob = viewModelScope.launch {
				_isDisabled.emit(true)
				if (deleteList.isNotEmpty()) deleteUserCities(deleteList)
				_isDisabled.emit(false)
			}
		}
	}
	
	fun addDelete(coordinates: Coordinates) {
		deleteList.add(coordinates)
	}
	
	fun removeDelete(coordinates: Coordinates) {
		deleteList.remove(coordinates)
	}
	
}