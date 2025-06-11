package com.example.reshare.presentation.features.mainGraph.explore.mapPager

import com.google.android.gms.maps.model.LatLng

data class ExploreMapState (
    val selectedLocation: LatLng = LatLng(21.0049, 105.8431),
    val radius: Float = 3f
)