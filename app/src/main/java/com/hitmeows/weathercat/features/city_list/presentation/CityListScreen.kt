package com.hitmeows.weathercat.features.city_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hitmeows.weathercat.features.city_list.UserCityWithWeather
import com.hitmeows.weathercat.ui.theme.Shapes

@Composable
fun CityListScreen(
	viewModel: CityListViewModel = hiltViewModel()
) {
	val state = viewModel.state
	if (state.value.isLoading) CircularProgressIndicator()
	if (state.value.isError) Text(text = state.value.errorMessage)
	
	
	LazyColumn {
		items(state.value.cityList) {
			ListItem(city = it)
		}
	}
	
}

@Composable
fun ListItem(city: UserCityWithWeather) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(110.dp)
			.padding(10.dp)
			.clip(AbsoluteRoundedCornerShape(5.dp))
			.background(
				if (city.weatherId == 800) Color.Cyan
				else if (city.weatherId
						.toString()
						.startsWith("80")
				)
					Color.Gray
				else Color.White
			)
			.border(3.dp, Color.Black, AbsoluteRoundedCornerShape(5.dp))
			.padding(20.dp)
	) {
		Row(
			Modifier.fillMaxSize(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = city.name, fontWeight = FontWeight.ExtraBold)
			Column(
				verticalArrangement = Arrangement.SpaceAround
			) {
				Text(text = "${city.temp}°C")
				Spacer(modifier = Modifier.height(10.dp))
				Text(text = city.weatherDescription)
			}
			
		}
	}
}