package com.hitmeows.weathercat.features.search.presentation

import android.location.Location
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hitmeows.weathercat.features.search.domain.SearchedCity

@Composable
fun SearchScreen(
	navController: NavController,
	getLocation: () -> Location?,
	viewModel: SearchScreenViewModel = hiltViewModel()
) {
	val state = viewModel.citiesState.value
	val isLoading = viewModel.isLoading.collectAsState()
	
	BackHandler(enabled = isLoading.value) {
	
	}
	
	Scaffold(topBar = {
		TopAppBar(title = {
			Text(text = "Search City")
		})
	}) {
		if (isLoading.value) CircularProgressIndicator()
		Column(
			Modifier
				.fillMaxSize()
				.padding(20.dp)
		) {
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
	onSearch: (String) -> Unit
) {
	var textToSearch by remember {
		mutableStateOf("")
	}
	val keyboard = LocalSoftwareKeyboardController.current
	TextField(value = textToSearch,
		onValueChange = {
			textToSearch = it
		},
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Search
		),
		keyboardActions = KeyboardActions(
			onSearch = {
				onSearch(textToSearch)
				keyboard?.hide()
			}
		),
		modifier = Modifier.fillMaxWidth(),
		trailingIcon = {
			IconButton(onClick = { onSearch(textToSearch) }) {
				Icon(imageVector = Icons.Default.Search, contentDescription = "")
			}
		}
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
	LazyColumn {
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

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CityItem(
	text: String,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(0.dp, 10.dp, 0.dp, 0.dp)
	) {
		
		TextButton(onClick = { onClick() }) {
			Text(text = text, fontSize = TextUnit(17f, TextUnitType.Sp))
		}
	}
}

@OptIn(
	ExperimentalPermissionsApi::class, androidx.compose.ui.unit.ExperimentalUnitApi::class
)
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
	
	Card(modifier = Modifier.fillMaxWidth()) {
		
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
			Row {
				Text(text = "Current", fontSize = TextUnit(19f, TextUnitType.Sp))
				Spacer(modifier = Modifier.width(5.dp))
				Icon(imageVector = Icons.Default.LocationOn, contentDescription = "")
			}
		}
	}
}