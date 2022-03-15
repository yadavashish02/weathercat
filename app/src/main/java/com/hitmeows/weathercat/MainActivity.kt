package com.hitmeows.weathercat

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hitmeows.weathercat.features.search.presentation.SearchScreen
import com.hitmeows.weathercat.ui.theme.WeatherCatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private lateinit var fusedLocationsClient: FusedLocationProviderClient
	private var location: MutableState<Location?> = mutableStateOf(null)
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fusedLocationsClient = LocationServices.getFusedLocationProviderClient(this)
		
		setContent {
			WeatherCatTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					SearchScreen(getLocation = {
						getLocation()
						location.value
					})
				}
			}
		}
	}
	
	private fun getLocation() {
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_COARSE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			fusedLocationsClient.lastLocation.addOnSuccessListener {
				location.value = it
			}
		}
	}
}

@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	WeatherCatTheme {
		Greeting("Android")
	}
}