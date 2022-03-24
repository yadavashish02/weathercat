package com.hitmeows.weathercat.features.weather.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.weathercat.common.UpdateWeather
import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity
import com.hitmeows.weathercat.features.weather.use_cases.WeatherUseCases
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
class WeatherViewModel @Inject constructor(
	private val useCases: WeatherUseCases,
	private val updateWeather: UpdateWeather
) : ViewModel() {
	
	init {
		getAllCities()
	}
	
	private val _isRefreshing = MutableStateFlow(false)
	val isRefreshing: StateFlow<Boolean>
		get() = _isRefreshing.asStateFlow()
	
	private val _list = mutableStateOf(emptyList<UserCity>())
	val list: State<List<UserCity>> = _list
	
	private val _currentWeatherState = mutableStateOf(CurrentWeatherState())
	val currentWeatherState: State<CurrentWeatherState> = _currentWeatherState
	
	private val _hourlyWeatherState = mutableStateOf(HourlyWeatherState())
	val hourlyWeatherState: State<HourlyWeatherState> = _hourlyWeatherState
	
	private val _dailyWeatherState = mutableStateOf(DailyWeatherState())
	val dailyWeatherState: State<DailyWeatherState> = _dailyWeatherState
	
	private var allCitiesJob: Job? = null
	private var currentWeatherJob: Job? = null
	private var hourlyJob: Job? = null
	private var dailyJob: Job? = null
	private var updateJob: Job? = null
	private var deleteJob: Job? = null
	
	
	private fun getAllCities() {
		allCitiesJob?.cancel()
		allCitiesJob = viewModelScope.launch {
			useCases.getAllUserCities().collectLatest {
				_list.value = it
				if (it.isNotEmpty()) {
					getCurrentWeather(it[0].coordinates.lat, it[0].coordinates.lon)
					getHourly(it[0].coordinates.lat, it[0].coordinates.lon)
					getDaily(it[0].coordinates.lat, it[0].coordinates.lon)
				}
			}
		}
	}
	
	fun getCurrentWeather(lat: Double, lon: Double) {
		currentWeatherJob?.cancel()
		_currentWeatherState.value = CurrentWeatherState()
		currentWeatherJob = viewModelScope.launch(Dispatchers.IO) {
			try {
				_currentWeatherState.value =
					CurrentWeatherState(weather = useCases.getCurrent(lat, lon))
			} catch (e: Exception) {
				_currentWeatherState.value = CurrentWeatherState(
					isError = true,
					errorMessage = e.message ?: "error occurred"
				)
			}
			
		}
	}
	
	fun getHourly(lat: Double, lon: Double) {
		hourlyJob?.cancel()
		hourlyJob = viewModelScope.launch(Dispatchers.IO) {
			try {
				useCases.getHourly(lat, lon).collectLatest {
					_hourlyWeatherState.value = HourlyWeatherState(weather = it)
				}
			} catch (e: Exception) {
				_hourlyWeatherState.value =
					HourlyWeatherState(isError = true, errorMessage = e.message ?: "error occurred")
			}
		}
	}
	
	fun getDaily(lat: Double, lon: Double) {
		dailyJob?.cancel()
		dailyJob = viewModelScope.launch(Dispatchers.IO) {
			try {
				useCases.getDaily(lat, lon).collectLatest {
					_dailyWeatherState.value = DailyWeatherState(weather = it)
				}
			} catch (e: Exception) {
				_dailyWeatherState.value =
					DailyWeatherState(isError = true, errorMessage = e.message ?: "error occurred")
			}
		}
	}
	
	fun update() {
		if (updateJob == null || updateJob?.isCompleted == true) {
			updateJob = viewModelScope.launch(Dispatchers.IO) {
				_isRefreshing.emit(true)
				updateWeather.invoke()
				_isRefreshing.emit(false)
			}
		}
	}
	
	fun update(lat: Double, lon: Double) {
		if (updateJob == null || updateJob?.isCompleted == true) {
			updateJob = viewModelScope.launch(Dispatchers.IO) {
				_isRefreshing.emit(true)
				updateWeather.single(lat, lon)
				Log.d("update", "updated")
				_isRefreshing.emit(false)
			}
		}
	}
	
	
}