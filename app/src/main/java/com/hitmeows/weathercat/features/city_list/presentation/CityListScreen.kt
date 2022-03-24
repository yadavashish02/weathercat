package com.hitmeows.weathercat.features.city_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hitmeows.weathercat.features.city_list.UserCityWithWeather
import com.hitmeows.weathercat.features.weather.data.local.Coordinates

@Composable
fun CityListScreen(
	navController: NavController,
	viewModel: CityListViewModel = hiltViewModel()
) {
	val state = viewModel.state
	val isRefreshing = viewModel.isRefreshing.collectAsState()
	val isDisabled = viewModel.isDisabled.collectAsState()
	if (state.value.isLoading) CircularProgressIndicator()
	if (state.value.isError) Text(text = state.value.errorMessage)
	var edit by remember {
		mutableStateOf(false)
	}
	
	Scaffold(
		topBar = {
			TopAppBar(title = {
				Text(text = "Cities List")
			}, actions = {
				if (edit) {
					IconButton(onClick = {
						viewModel.delete()
						edit = !edit
					}, enabled = !isDisabled.value) {
						Icon(imageVector = Icons.Default.Delete, contentDescription = "")
					}
				} else {
					IconButton(onClick = { edit = !edit }, enabled = !isDisabled.value) {
						Icon(imageVector = Icons.Default.Edit, contentDescription = "")
					}
				}
			})
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = { navController.navigate("search") },
				backgroundColor = Color.Gray,
			) {
				Icon(imageVector = Icons.Default.Add, contentDescription = "")
			}
		},
		floatingActionButtonPosition = FabPosition.End
	) {
		SwipeRefresh(
			state = rememberSwipeRefreshState(
				isRefreshing = isRefreshing.value
			),
			onRefresh = { viewModel.update() },
			modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)
		) {
			LazyColumn {
				items(state.value.cityList) {
					ListItem(
						city = it,
						it.coordinates == state.value.cityList[0].coordinates,
						edit
					) { coord, isDelete ->
						if (isDelete) {
							viewModel.addDelete(coord)
						} else {
							viewModel.removeDelete(coord)
						}
					}
				}
			}
		}
	}
	
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ListItem(
	city: UserCityWithWeather,
	isCurrent: Boolean,
	isEdit: Boolean,
	onDelete: (Coordinates, Boolean) -> Unit
) {
	var checked by remember {
		mutableStateOf(false)
	}
	
	
	val haze = Color(82,72,50)
	val rain = Color(36,60,76)
	val clear = Color(122,160,204)
	val clouds = Color.DarkGray
	val snow = Color.White.copy(0.5f)
	
	val id = city.weatherId.toString()
	var color by remember {
		mutableStateOf(Color.Transparent)
	}
	
	color = if (id.startsWith("2") || id.startsWith("3") || id.startsWith("5")) rain
	else if (id.startsWith("7")) haze
	else if (id.startsWith("800")) clear
	else if (id.startsWith("8")) clouds
	else if (id.startsWith("6")) snow
	else Color.Transparent
	
	
	Card(Modifier.padding(10.dp, 20.dp, 10.dp, 0.dp)) {
		Box(
			modifier = Modifier
				.height(100.dp)
				.clip(AbsoluteRoundedCornerShape(5.dp))
				.background(
					color
				)
				.padding(20.dp)
		) {
			Row(
				Modifier.fillMaxSize(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Row() {
					Text(
						text = city.name, fontWeight = FontWeight.Light,
						fontSize = TextUnit(26f, TextUnitType.Sp)
					)
					if (isCurrent) {
						Spacer(modifier = Modifier.width(5.dp))
						Icon(
							imageVector = Icons.Default.LocationOn,
							contentDescription = "",
							tint = MaterialTheme.colors.primary
						)
					}
					
				}
				Column(
					verticalArrangement = Arrangement.SpaceEvenly
				) {
					Text(text = "${city.temp}°C")
					Spacer(modifier = Modifier.height(10.dp))
					Text(text = city.weatherDescription)
				}
				if (isEdit) {
					Spacer(modifier = Modifier.width(5.dp))
					Checkbox(checked = checked, onCheckedChange = {
						checked = !checked
						onDelete(city.coordinates, checked)
					})
				}
				
			}
		}
	}
}