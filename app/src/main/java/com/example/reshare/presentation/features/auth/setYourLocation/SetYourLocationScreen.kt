package com.example.reshare.presentation.features.auth.setYourLocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reshare.domain.model.PlaceSuggestion
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightPurple
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("MissingPermission")
@Composable
fun SetYourLocationScreen(
    viewModel: SetYourLocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            state.selectedLocation, state.zoomLevel
        )
    }
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    LaunchedEffect(state.selectedLocation) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(state.selectedLocation, state.zoomLevel)
        )
    }
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .collectLatest { position  ->
                viewModel.onEvent(SetYourLocationUiEvent.OnCameraMoved(position.target))
                viewModel.onEvent(SetYourLocationUiEvent.OnZoomChanged(position.zoom))
            }
    }

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isLocationEnabled) {
                Toast.makeText(context, "Please enable Location (GPS) in Settings",
                    Toast.LENGTH_LONG).show()
                return@rememberLauncherForActivityResult
            }

            viewModel.onEvent(
                SetYourLocationUiEvent.OnSuggestionSelectedWithLatLng(
                    latLng = state.selectedLocation,
                    isRequesting = true
                )
            )
            // Dùng API LocationRequest.Builder mới
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10000L // intervalMillis
            )
                .setWaitForAccurateLocation(true)
                .setMinUpdateIntervalMillis(5000L)
                .setMaxUpdateDelayMillis(15000L)
                .setMaxUpdates(1)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    val latLng = if (location != null) {
                        LatLng(location.latitude, location.longitude)
                    } else {
                        Toast.makeText(
                            context,
                            "Still unable to get current location",
                            Toast.LENGTH_SHORT
                        ).show()
                        state.selectedLocation
                    }

                    viewModel.onEvent(
                        SetYourLocationUiEvent.OnSuggestionSelectedWithLatLng(
                            latLng = latLng,
                            isRequesting = false
                        )
                    )
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.White)
        ) {
            LocationSelectorHeader(
                query = state.searchQuery,
                onQueryChange = { viewModel.onEvent(SetYourLocationUiEvent.OnQueryChange(it)) },
                onUseCurrentLocation = { permissionLauncher.launch(locationPermissions) },
                isRequestingLocation = state.isRequestingLocation,
                searchResults = state.suggestions,
                onResultClick = { suggestion ->
                    viewModel.onEvent(SetYourLocationUiEvent.OnSuggestionSelected(suggestion.placeId))
                },
                onSetLocation = {
                    viewModel.onEvent(SetYourLocationUiEvent.SetLocation(
                        state.selectedLocation.latitude, state.selectedLocation.longitude
                    ))
                },
                isLoading = state.isLoading,
                cameraPositionState = cameraPositionState
            )
        }
    }
}

@Composable
fun LocationSelectorHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onUseCurrentLocation: () -> Unit = {},
    isRequestingLocation: Boolean = false,
    searchResults: List<PlaceSuggestion>,
    onResultClick: (PlaceSuggestion) -> Unit,
    onSetLocation: () -> Unit,
    isLoading: Boolean = false,
    cameraPositionState: CameraPositionState
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Set your location",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Don’t worry, we won’t share this with anyone else.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search here") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF2F2F2)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledContainerColor = Color(0xFFF2F2F2)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ){
            GoogleMap(
                modifier = Modifier.fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomGesturesEnabled = false
                ),
            ) {}

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Center Marker",
                tint = DarkPurple,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(46.dp)
            )

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            if (!isRequestingLocation) { onUseCurrentLocation() }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = if (!isRequestingLocation) DarkPurple else LightPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Locate me",
                        color = if (!isRequestingLocation) DarkPurple else LightPurple,
                        fontWeight = FontWeight.Medium
                    )

                    if (isRequestingLocation) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (searchResults.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White)
                ) {
                    searchResults.forEach { item ->
                        Text(
                            text = item.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onResultClick(item) }
                                .padding(12.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = { if(!isLoading) onSetLocation() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkPurple,
                contentColor = Color.White
            )
        ) {
            Text("Set home location")
            if (isLoading) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}