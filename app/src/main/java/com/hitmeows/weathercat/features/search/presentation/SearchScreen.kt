package com.hitmeows.weathercat.features.search.presentation

import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
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
	viewModel: SearchScreenViewModel = hiltViewModel()
) {
	val state = viewModel.citiesState.value
	
	SearchBar(onSearch = {
		viewModel.searchCity(it)
	})
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
			onSearch = {onSearch(textToSearch)}
		)
	//todo
	//trailing and leading icons
	)
}

@Composable
fun CitiesColumn(
	citiesState: SearchedCitiesState,
	onItemClick: (Double,Double) -> Unit
) {
	if (citiesState.isLoading) CircularProgressIndicator()
	if (citiesState.isError) Text(text = citiesState.errorMessage)
	LazyColumn() {
		item {
		
		}
		
		items(citiesState.citiesList) {
			CityItem(
				text = it.cityName +
						if (it.stateName.isNotBlank()) ", ${it.stateName}"
						else "" +
						", ${it.countryName}"
			) {
				onItemClick(it.lat,it.lon)
			}
		}
	}
}

@Composable
fun CityItem(
	text: String,
	onClick: () -> Unit
) {
	Button(onClick = { onClick() }) {
		Text(text = text)
	}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentCity(
	onClick: (Double,Double,Boolean) -> Unit
) {
	val locationPermissionState = rememberPermissionState(
		permission = android.Manifest.permission.ACCESS_COARSE_LOCATION)
	
	if (locationPermissionState.status.isGranted) {
	
	}
	Button(onClick = {  }) {
		Row() {
			Text(text = "Current")
			//todo
			//icon
		}
	}
}