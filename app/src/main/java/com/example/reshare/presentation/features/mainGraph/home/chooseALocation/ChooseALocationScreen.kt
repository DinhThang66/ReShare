package com.example.reshare.presentation.features.mainGraph.home.chooseALocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.reshare.domain.model.PlaceSuggestion
import com.example.reshare.presentation.utils.calculateBounds
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightPurple
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter",
    "MissingPermission"
)
@Composable
fun ChooseALocationScreen(
    navController: NavController,
    viewModel: ChooseALocationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

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
                ChooseALocationUiEvent.OnSuggestionSelectedWithLatLng(
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
                        ChooseALocationUiEvent.OnSuggestionSelectedWithLatLng(
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

            /*
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    viewModel.onEvent(RadiusMapUiEvent.OnSuggestionSelectedWithLatLng(latLng))
                } else {
                    Toast.makeText(
                        context,
                        "Unable to get current location",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.onEvent(RadiusMapUiEvent.OnSuggestionSelectedWithLatLng(state.selectedLocation)) // fallback
                }
            }
             */
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Automatically align display area
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { event ->
            when (event) {
                is ChooseALocationSideEffect.ShowError ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is ChooseALocationSideEffect.CloseScreen -> navController.popBackStack()
                is ChooseALocationSideEffect.LocationUpdated -> {
                    val bounds = calculateBounds(state.selectedLocation, state.radiusKm * 1000f)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngBounds(bounds, 50)
                    )
                }
                is ChooseALocationSideEffect.LocationUpdatedAndClose -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                    navController.popBackStack()
                }
            }
        }
    }
    // Initial camera update
    LaunchedEffect(state.selectedLocation, state.radiusKm) {
        val bounds = calculateBounds(state.selectedLocation, state.radiusKm * 1000f)
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngBounds(bounds, 50)
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            LocationSelectorHeader(
                query = state.searchQuery,
                onQueryChange = { viewModel.onEvent(ChooseALocationUiEvent.OnQueryChange(it)) },
                onClose = { navController.popBackStack() },
                onUseCurrentLocation = { permissionLauncher.launch(locationPermissions) },
                isRequestingLocation = state.isRequestingLocation,
                searchResults = state.suggestions,
                onResultClick = { suggestion ->
                    viewModel.onEvent(ChooseALocationUiEvent.OnSuggestionSelected(suggestion.placeId))
                }
            )
            GoogleMap(
                modifier = Modifier.fillMaxWidth().height(350.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = state.selectedLocation))
                Circle(
                    center = state.selectedLocation,
                    radius = (state.radiusKm * 1000f).toDouble(),
                    fillColor = Color(0x334285F4), // Semi-transparent purple
                    strokeColor = Color(0xFF4285F4),
                    strokeWidth = 2f
                )
            }

            DistanceSelector(
                value = state.radiusKm,
                onValueChange = {
                    viewModel.onEvent(ChooseALocationUiEvent.OnRadiusChange(it))
                },
                onApply = {
                    viewModel.onEvent(ChooseALocationUiEvent.OnApply(state.selectedLocation, state.radiusKm))
                },
                isLoading = state.isLoading
            )
        }
    }
}

@Composable
fun LocationSelectorHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit = {},
    onUseCurrentLocation: () -> Unit = {},
    isRequestingLocation: Boolean = false,
    searchResults: List<PlaceSuggestion>,
    onResultClick: (PlaceSuggestion) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Row: Title + Close button
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Choose a location to see\nwhat's available near you",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center).padding(top = 16.dp)
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
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

        Spacer(modifier = Modifier.height(8.dp))

        if (searchResults.isNotEmpty()) {
            // Optional: hiện danh sách gợi ý
            Column {
                searchResults.forEach { item ->
                    Text(
                        text = item.description,
                        modifier = Modifier
                            .clickable { onResultClick(item) }
                            .padding(12.dp)
                    )
                }
            }
        } else {
            // Use current location
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if(!isRequestingLocation) onUseCurrentLocation() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Use current location",
                    tint = if (!isRequestingLocation ) DarkPurple else LightPurple
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Or use current location",
                    color = if (!isRequestingLocation ) DarkPurple else LightPurple,
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
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DistanceSelector(
    value: Float,
    onValueChange: (Float) -> Unit,
    onApply: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Select a distance",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0.5f..25f,
                steps = 49,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = String.format("%.1f km", value),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50), // bo tròn toàn phần
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3F0071),
                contentColor = Color.White
            )
        ) {
            Text("Apply")

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
