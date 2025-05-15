package com.example.reshare.presentation.features.mainGraph.explore.listPager

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.reshare.R
import com.example.reshare.presentation.components.ExploreListItem

@Composable
fun ExploreListPager(modifier: Modifier = Modifier) {
    LazyColumn() {
        items(10) {
            ExploreListItem(
                imageRes = R.drawable.img,
                price = "20,00£",
                isNew = true,
                title = "3 x British Basil in a pot",
                userName = "Neti",
                rating = 4.7f,
                distance = "14.4km",
                isFree = false
            )

            ExploreListItem(
                imageRes = R.drawable.img_1,
                price = null,
                isNew = true,
                title = "Mop holder",
                userName = "Shaunak",
                rating = null,
                distance = "8.7km",
                isFree = true
            )
        }
    }
}