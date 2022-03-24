package com.hitmeows.weathercat.features.weather.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hitmeows.weathercat.R
import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
	navController: NavController,
	viewModel: WeatherViewModel = hiltViewModel()
) {
	val listState = viewModel.list
	val weatherState = viewModel.currentWeatherState
	val config = LocalConfiguration.current
	val screenHeight = config.screenHeightDp
	val hourlyWeatherState = viewModel.hourlyWeatherState
	val dailyWeatherState = viewModel.dailyWeatherState
	val selectedCoordinates = remember {
		mutableStateOf(Coordinates(0.0, 0.0))
	}
	val isLaunched = remember {
		mutableStateOf(true)
	}
	
	LaunchedEffect(key1 = listState.value) {
		delay(1000)
		if (listState.value.isEmpty()) navController.navigate("search")
	}
	
	val isRefreshing = viewModel.isRefreshing.collectAsState()
	val scrollState = rememberScrollState()
	val haze = Color(82,72,50)
	val rain = Color(36,60,76)
	val clear = Color(122,160,204)
	val clouds = Color.DarkGray
	val snow = Color.White.copy(0.5f)
	
	var color by remember {
		mutableStateOf(Color.Transparent)
	}
	
	
	
	
	Scaffold(topBar = {
		TopAppBar(title = {
			Text(text = "Weather")
		}, actions = {
			IconButton(onClick = { navController.navigate("city_list") }) {
				Icon(imageVector = Icons.Default.List, contentDescription = "")
			}
		},
			backgroundColor = color
		)
	}
	) {
		if (listState.value.isNotEmpty()) {
			val id = weatherState.value.weather.weatherId.toString()
			color = if (id.startsWith("2") || id.startsWith("3") || id.startsWith("5")) rain
					else if (id.startsWith("7")) haze
					else if (id.startsWith("800")) clear
					else if (id.startsWith("8")) clouds
					else if (id.startsWith("6")) snow
					else Color.Transparent
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height((screenHeight / 1.5).dp)
					.background(
						Brush.verticalGradient(
							listOf(
								color,
								Color.Transparent
							)
						)
					)
			)
			Column {
					LazyRow(Modifier.fillMaxWidth()) {
						items(listState.value) {
							Button(
								onClick = {
									isLaunched.value = false
									selectedCoordinates.value = it.coordinates
									viewModel.getCurrentWeather(
										it.coordinates.lat,
										it.coordinates.lon
									)
									viewModel.getHourly(
										it.coordinates.lat,
										it.coordinates.lon
									)
									viewModel.getDaily(
										it.coordinates.lat,
										it.coordinates.lon
									)
								},
								modifier = Modifier.padding(5.dp, 10.dp),
								colors = ButtonDefaults.buttonColors(
									contentColor = Color.White,
									backgroundColor = if (it.coordinates == selectedCoordinates.value || isLaunched.value && it.coordinates == listState.value[0].coordinates) {
										MaterialTheme.colors.primary
									} else color
								)
							) {
								Text(text = it.name)
							}
						}
					}
				
				Divider(color = color.copy(0.8f))
				
				SwipeRefresh(
					state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
					onRefresh = {
						if (isLaunched.value) selectedCoordinates.value =
							listState.value[0].coordinates
						viewModel.update(
							selectedCoordinates.value.lat,
							selectedCoordinates.value.lon
						)
					}
				) {
					Column(
						Modifier
							.padding(5.dp, 0.dp, 5.dp, 10.dp)
							.verticalScroll(
								scrollState
							)
					) {
						val weather = weatherState.value.weather
						Spacer(modifier = Modifier.height((screenHeight / 4).dp))
						TemperaturePanel(
							temp = weather.currentTemp,
							des = weather.weatherDescription,
							weather.minTemp,
							weather.maxTemp,
							weather.dt,
							weather.timezone.toInt()
						)
						HourlyPanel(hourlyWeatherState = hourlyWeatherState.value)
						DailyPanel(dailyWeatherState = dailyWeatherState.value)
						WeatherDetails(weatherState = weatherState.value)
					}
				}
				
			}
			
		}
		
	}
	
	
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TemperaturePanel(
	temp: Int,
	des: String,
	min: Int,
	max: Int,
	dt: Long,
	tz: Int
) {
	
	val now = LocalDateTime.ofEpochSecond(dt, 0, ZoneOffset.ofTotalSeconds(tz))
	val month = now.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
	val date = now.dayOfMonth.toString()
	val day = now.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
	Column() {
		Row(
			Modifier
				.padding(20.dp, 10.dp, 10.dp, 5.dp)
				.height(IntrinsicSize.Min)
		) {
			Text(
				text = temp.toString(),
				fontWeight = FontWeight.Light,
				fontSize = TextUnit(90f, TextUnitType.Sp),
				fontFamily = FontFamily.SansSerif,
				fontStyle = FontStyle.Normal
			)
			
			Column(
				Modifier
					.fillMaxHeight()
					.padding(8.dp, 15.dp, 0.dp, 15.dp),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "°C",
					fontSize = TextUnit(30f, TextUnitType.Sp)
				)
				Text(
					text = des,
					fontSize = TextUnit(30f, TextUnitType.Sp)
				)
			}
		}
		Text(
			text = "$month $date, $day    $min°C / $max°C",
			modifier = Modifier.padding(20.dp, 0.dp, 0.dp, 5.dp)
		)
	}
}

@Composable
fun WeatherDetails(
	weatherState: CurrentWeatherState
) {
	val weather = weatherState.weather
	Column(Modifier.padding(0.dp, 40.dp)) {
		Text(text = "Weather Details", Modifier.padding(20.dp, 10.dp, 10.dp, 0.dp))
		Row(
			Modifier
				.fillMaxWidth()
				.padding(30.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			WeatherDetailItem(title = "Apparent Temperature", detail = "${weather.feelsLike}°C")
			WeatherDetailItem(title = "Humidity", detail = "${weather.humidity.roundToInt()}%")
		}
		Row(
			Modifier
				.fillMaxWidth()
				.padding(30.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			val deg = weather.windDirection
			val wind = if (deg in 11.25..33.75) "NNE"
			else if (deg in 33.76..56.25) "NE"
			else if (deg in 56.25..78.75) "ENE"
			else if (deg in 78.75..101.25) "E"
			else if (deg in 101.25..123.75) "ESE"
			else if (deg in 123.75..146.25) "SE"
			else if (deg in 146.25..168.75) "SSE"
			else if (deg in 168.75..191.25) "S"
			else if (deg in 191.25..213.75) "SSW"
			else if (deg in 213.75..236.25) "SW"
			else if (deg in 236.25..258.75) "WSW"
			else if (deg in 258.75..281.25) "W"
			else if (deg in 281.25..303.75) "WNW"
			else if (deg in 303.75..326.25) "NW"
			else if (deg in 326.25..348.75) "NNW"
			else "N"
			WeatherDetailItem(
				title = "$wind Wind", detail = "${weather.windSpeed.roundToInt()} km/h"
			)
			WeatherDetailItem(
				title = "Air Pressure",
				detail = "${weather.pressure.roundToInt()} hPa"
			)
		}
	}
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun WeatherDetailItem(
	title: String,
	detail: String
) {
	Column {
		Text(text = title, fontWeight = FontWeight.Light, color = Color.Gray)
		Spacer(modifier = Modifier.height(3.dp))
		Text(
			text = detail,
			fontSize = TextUnit(25f, TextUnitType.Sp),
			fontWeight = FontWeight.Light
		)
	}
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun HourlyPanel(
	hourlyWeatherState: HourlyWeatherState
) {
	
	val map = mapOf(
		"01d" to R.drawable._01d,
		"01n" to R.drawable._01n,
		"02d" to R.drawable._02d,
		"02n" to R.drawable._02n,
		"03d" to R.drawable._03d,
		"03n" to R.drawable._03n,
		"04d" to R.drawable._04d,
		"04n" to R.drawable._04n,
		"09d" to R.drawable._09d,
		"09n" to R.drawable._09n,
		"10d" to R.drawable._10d,
		"10n" to R.drawable._10n,
		"11d" to R.drawable._11d,
		"11n" to R.drawable._11n,
		"13d" to R.drawable._13d,
		"13n" to R.drawable._13n,
		"50d" to R.drawable._50d,
		"50n" to R.drawable._50n
	)
	
	Log.d("wtfff", hourlyWeatherState.weather.toString())
	LazyRow(contentPadding = PaddingValues(10.dp, 40.dp, 15.dp, 40.dp)) {
		if (hourlyWeatherState.weather.isNotEmpty()) {
			items(hourlyWeatherState.weather.subList(0, 23)) {
				Column(
					Modifier.padding(10.dp, 0.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "${
							LocalDateTime.ofEpochSecond(
								it.dt,
								0,
								ZoneOffset.ofTotalSeconds(it.timezone.toInt())
							).hour
						}:00",
						fontSize = TextUnit(13f, TextUnitType.Sp),
						fontWeight = FontWeight.Light
					)
					Image(
						painter = painterResource(id = map[it.weatherIcon] ?: R.drawable._01d),
						contentDescription = "",
						Modifier.padding(0.dp, 5.dp)
					)
					Text(
						text = "${it.temp}°C",
						fontSize = TextUnit(13f, TextUnitType.Sp),
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}

@Composable
fun DailyPanel(
	dailyWeatherState: DailyWeatherState
) {
	val map = mapOf(
		"01d" to R.drawable._01d,
		"01n" to R.drawable._01n,
		"02d" to R.drawable._02d,
		"02n" to R.drawable._02n,
		"03d" to R.drawable._03d,
		"03n" to R.drawable._03n,
		"04d" to R.drawable._04d,
		"04n" to R.drawable._04n,
		"09d" to R.drawable._09d,
		"09n" to R.drawable._09n,
		"10d" to R.drawable._10d,
		"10n" to R.drawable._10n,
		"11d" to R.drawable._11d,
		"11n" to R.drawable._11n,
		"13d" to R.drawable._13d,
		"13n" to R.drawable._13n,
		"50d" to R.drawable._50d,
		"50n" to R.drawable._50n
	)
	if (dailyWeatherState.weather.isNotEmpty()) {
		Column {
			Row(
				Modifier
					.fillMaxWidth()
					.padding(20.dp, 5.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				Alignment.CenterVertically
			) {
				Text(text = "Tod")
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(text = dailyWeatherState.weather[0].weatherDescription)
					Image(
						painter = painterResource(
							id = map[dailyWeatherState.weather[0].weatherIcon]
								?: R.drawable._01d
						), contentDescription = ""
					)
				}
				Text(text = "${dailyWeatherState.weather[0].maxTemp} / ${dailyWeatherState.weather[0].minTemp}°C")
			}
			Row(
				Modifier
					.fillMaxWidth()
					.padding(20.dp, 5.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				Alignment.CenterVertically
			) {
				Text(text = "Tom")
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(text = dailyWeatherState.weather[1].weatherDescription)
					Image(
						painter = painterResource(
							id = map[dailyWeatherState.weather[1].weatherIcon]
								?: R.drawable._01d
						), contentDescription = ""
					)
				}
				Text(text = "${dailyWeatherState.weather[1].maxTemp} / ${dailyWeatherState.weather[1].minTemp}°C")
			}
			DailyItem(
				index = 2,
				dailyWeatherState = dailyWeatherState,
				map[dailyWeatherState.weather[2].weatherIcon] ?: R.drawable._01d
			)
			DailyItem(
				index = 3,
				dailyWeatherState = dailyWeatherState,
				map[dailyWeatherState.weather[3].weatherIcon] ?: R.drawable._01d
			)
			DailyItem(
				index = 4,
				dailyWeatherState = dailyWeatherState,
				map[dailyWeatherState.weather[4].weatherIcon] ?: R.drawable._01d
			)
			DailyItem(
				index = 5,
				dailyWeatherState = dailyWeatherState,
				map[dailyWeatherState.weather[5].weatherIcon] ?: R.drawable._01d
			)
			DailyItem(
				index = 6,
				dailyWeatherState = dailyWeatherState,
				map[dailyWeatherState.weather[6].weatherIcon] ?: R.drawable._01d
			)
			
		}
	}
}

@Composable
fun DailyItem(
	index: Int,
	dailyWeatherState: DailyWeatherState,
	id: Int
) {
	Row(
		Modifier
			.fillMaxWidth()
			.padding(20.dp, 5.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = LocalDateTime.ofEpochSecond(
				dailyWeatherState.weather[index].dt,
				0,
				ZoneOffset.ofTotalSeconds(dailyWeatherState.weather[index].timezone.toInt())
			).dayOfWeek.getDisplayName(
				TextStyle.SHORT,
				Locale.getDefault()
			)
		)
		Row(verticalAlignment = Alignment.CenterVertically) {
			
			Text(text = dailyWeatherState.weather[index].weatherDescription)
			Image(painter = painterResource(id = id), contentDescription = "")
			
		}
		Text(text = "${dailyWeatherState.weather[index].maxTemp} / ${dailyWeatherState.weather[index].minTemp}°C")
	}
}