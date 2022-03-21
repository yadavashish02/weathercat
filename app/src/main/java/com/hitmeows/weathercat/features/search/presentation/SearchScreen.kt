package com.hitmeows.weathercat.features.search.presentation

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hitmeows.weathercat.features.search.domain.SearchedCity

@Composable
fun SearchScreen(
	getLocation: () -> Location?,
	viewModel: SearchScreenViewModel = hiltViewModel()
) {
	val state = viewModel.citiesState.value
	
	Column() {
		SearchBar(onSearch = {
			viewModel.searchCity(it)
		})
		CitiesColumn(citiesState = state,
			onCurrentItemClick = { lat, lon ->
				viewModel.insertWeather(lat, lon)
			},
			onItemClick = {
				viewModel.insertWeather(it)
			}
		) {
			getLocation()
		}
	}
}

@Composable
fun SearchBar(
	onSearch: (String) -> Unit
) {
	var textToSearch by remember {
		mutableStateOf("")
	}
	TextField(value = textToSearch,
		onValueChange = {
			textToSearch = it
		},
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Search
		),
		keyboardActions = KeyboardActions(
			onSearch = { onSearch(textToSearch) }
		)
		//todo
		//trailing and leading icons
	)
}

@Composable
fun CitiesColumn(
	citiesState: SearchedCitiesState,
	onCurrentItemClick: (Double, Double) -> Unit,
	onItemClick: (SearchedCity) -> Unit,
	getLocation: () -> Location?
) {
	if (citiesState.isLoading) CircularProgressIndicator()
	if (citiesState.isError) Text(text = citiesState.errorMessage)
	LazyColumn() {
		item {
			CurrentCity(onClick = { lat, lon ->
				onCurrentItemClick(lat, lon)
			}) {
				getLocation()
			}
		}
		
		items(citiesState.citiesList) {
			CityItem(
				text = it.cityName +
						if (it.stateName.isNotBlank()) ", ${it.stateName}"
						else "" +
								", ${it.countryName}"
			) {
				onItemClick(it)
			}
		}
	}
}

@Composable
fun CityItem(
	text: String,
	onClick: () -> Unit
) {
	TextButton(onClick = { onClick() }) {
		Text(text = text)
	}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentCity(
	onClick: (Double, Double) -> Unit,
	getLocation: () -> Location?
) {
	
	var isButtonEnabled by remember {
		mutableStateOf(true)
	}
	val locationPermissionsState = rememberPermissionState(
		permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
	)
	
	val location: MutableState<Location?> = remember {
		mutableStateOf(null)
	}
	
	LaunchedEffect(key1 = location.value) {
		if (locationPermissionsState.status.isGranted) {
			if (location.value != null) isButtonEnabled = false
			location.value?.let { onClick(it.latitude, it.longitude) }
		}
	}
	
	TextButton(
		enabled = isButtonEnabled,
		onClick = {
			if (!locationPermissionsState.status.isGranted) {
				locationPermissionsState.launchPermissionRequest()
			}
			location.value = getLocation()
			if (location.value != null) isButtonEnabled = false
			location.value?.let {
				onClick(it.latitude, it.longitude)
			}
		}) {
		Row() {
			Text(text = "Current")
			//todo
			//icon
		}
	}
}