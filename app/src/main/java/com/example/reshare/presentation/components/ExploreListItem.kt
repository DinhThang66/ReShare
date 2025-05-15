package com.example.reshare.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reshare.R
import com.example.reshare.ui.theme.DarkGreen
import com.example.reshare.ui.theme.DarkPurple
import com.example.reshare.ui.theme.LightGreen
import com.example.reshare.ui.theme.LightPurple

@Composable
fun ExploreListItem(
    imageRes: Int,
    price: String?,
    isNew: Boolean,
    title: String,
    userName: String,
    rating: Float?,
    distance: String,
    isFree: Boolean
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(110.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row {
            Box(modifier = Modifier.width(110.dp).fillMaxHeight()){
                // Left Image
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp)
                        .size(24.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.StarOutline,
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .padding(end = 16.dp),

                    ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (price != null) {
                            Text(
                                text = price,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                style = TextStyle(lineHeight = 12.sp),
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (isFree) {
                            Text(
                                text = "Free",
                                color = DarkPurple,
                                fontSize = 12.sp,
                                style = TextStyle(lineHeight = 12.sp),
                                modifier = Modifier
                                    .background(LightPurple, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        if (isNew) {
                            Text(
                                text = "New",
                                color = DarkGreen,
                                fontSize = 12.sp,
                                style = TextStyle(lineHeight = 12.sp),
                                modifier = Modifier.background(LightGreen, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Title
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    //Seller & rating
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.img),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = userName, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        if (rating != null) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(text = rating.toString(), fontSize = 14.sp)
                        }
                    }

                    // Distance
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = distance, fontSize = 14.sp, color = Color.Black)
                    }

                }
            }


        }
    }
}

@Preview
@Composable
fun PreviewCardItem() {
    Column {
        ExploreListItem(
            imageRes = R.drawable.img,
            price = "20,00£",
            isNew = true,
            title = "3 x British Basil in a pot ddddddd",
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
