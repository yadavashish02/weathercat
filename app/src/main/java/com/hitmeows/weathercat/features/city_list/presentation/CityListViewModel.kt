package com.hitmeows.weathercat.features.city_list.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.city_list.use_cases.GetUserCitiesWithWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CityListViewModel @Inject constructor(
	private val getUserCitiesWithWeather: GetUserCitiesWithWeather
) : ViewModel() {
	private val _state = mutableStateOf(CityListState())
	val state: State<CityListState> = _state
	
	init {
		getCityList()
	}
	
	private var job: Job? = null
	
	fun getCityList() {
		job?.cancel()
		job = viewModelScope.launch(Dispatchers.IO) {
			getUserCitiesWithWeather.invoke().collectLatest {
				when (it) {
					is Resource.Loading -> {
						_state.value = CityListState(isLoading = true, cityList = it.data?: emptyList())
					}
					is Resource.Error -> {
						_state.value = CityListState(
							isError = true,
							errorMessage = it.exception?.message ?: "unknown error occurred",
							cityList = it.data?: emptyList()
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
	
}