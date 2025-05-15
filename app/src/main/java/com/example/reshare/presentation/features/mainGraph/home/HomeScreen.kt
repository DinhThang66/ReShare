package com.example.reshare.presentation.features.mainGraph.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.reshare.presentation.components.HomeItemCard
import com.example.reshare.presentation.utils.sectionTitles
import com.example.reshare.ui.theme.LightPurple

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
    ) {
        // Location on
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightPurple)
                .padding(horizontal = 16.dp)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Victoria Street",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "Listings within 20km",
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .offset(y = (-4).dp)
            )
        }

        // LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize() // Or .weight(1f)
                .background(Color.White),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(sectionTitles.size) { index ->
                Section(
                    title = sectionTitles[index],
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun Section(
    title: String,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "All",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp)
        ) {
            items(3) {
                HomeItemCard(navController = navController)
            }
        }
    }
}