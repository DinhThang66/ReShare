package com.example.reshare.presentation.features.mainGraph.explore.listPager

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.reshare.R
import com.example.reshare.domain.model.Product
import com.example.reshare.presentation.components.ExploreListItem

@SuppressLint("DefaultLocale")
@Composable
fun ExploreListPager(
    navController: NavController,
    products: List<Product>
) {
    LazyColumn() {
        items(products) {
            ExploreListItem(
                navController = navController,
                product = it,
                rating = 5.0f
            )
        }
    }
}